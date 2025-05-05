package com.demo.variety_store_mono.customer.service;

import com.demo.variety_store_mono.customer.dto.request.CartItemRequest;
import com.demo.variety_store_mono.customer.dto.response.CartResponse;
import com.demo.variety_store_mono.customer.entity.Cart;
import com.demo.variety_store_mono.customer.entity.CartItem;
import com.demo.variety_store_mono.customer.entity.CartItemOption;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.customer.repository.CartItemRepository;
import com.demo.variety_store_mono.customer.repository.CartRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.utility.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    /** 고객의 장바구니를 조회하거나, 없으면 생성하여 반환. */
    private Cart getOrCreateCart(Long customerId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        Customer customer = user.getCustomer();
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .customer(customer)
                            .build();

                    cartRepository.save(newCart);
                    return newCart;
                });
    }

    /** 장바구니 조회. */
    public CartResponse getCartDetails(Long customerId) {
        return CartMapper.toResponse(getOrCreateCart(customerId));
    }

    /** 장바구니에 상품 추가. */
    public void addItemToCart(Long customerId, CartItemRequest request) {

        Long productId = request.getProductId();    // 추가할 상품 ID
        int quantity = request.getQuantity();       // 추가할 수량
        List<Long> optionValueIds = request.getOptionValueIds();    // 선택한 옵션 값(optionValueIds) 목록

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        Cart cart = getOrCreateCart(customerId);

        // 상품 조회 (존재 여부 및 활성 상태 확인)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 기존 장바구니 목록에 이미 추가된 상품인지 확인.
        // (상품 ID와 옵션 구성까지 동일해야 같은 상품으로 판단)
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()) &&
                        areCartItemOptionsEqual(item, optionValueIds))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // 기존 항목이 있다면, 수량 증가.
            CartItem cartItem = existingCartItem.get();
            cartItem.updateQuantity(cartItem.getQuantity() + quantity);

            cartItem.finalUnitPrice();
            cart.finalTotalPrice();

            cartItemRepository.save(cartItem);
        } else {
            // 없으면 장바구니에 새 항목 생성.
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            // 만약 옵션이 선택된 경우, 각 옵션에 대해 CartItemOption을 생성해서 새 CartItem에 추가.
            if (optionValueIds != null && !optionValueIds.isEmpty()) {
                for (Long optionValueId : optionValueIds) {
                    // 선택한 옵션 값이 실제 존재하는지 확인.
                    ProductOptionValue pov = productRepository.findProductOptionValueById(optionValueId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션 값입니다."));

                    CartItemOption itemOption = new CartItemOption(pov);
                    newCartItem.addCartItemOption(itemOption);
                }
            }
            newCartItem.finalUnitPrice();
            cart.addCartItem(newCartItem);

            cart.finalTotalPrice();
            cartItemRepository.save(newCartItem);
        }
    }

    /**
     * 장바구니 항목 옵션이 입력한 옵션 값 ID 리스트와 동일한지 비교.
     * (옵션이 없으면 둘 모두 null 이거나, 빈 리스트여야 동일하다고 판단)
     */
    private boolean areCartItemOptionsEqual(CartItem cartItem, List<Long> optionValueIds) {
        List<Long> existingOptionIds = cartItem.getCartItemOptions().stream()
                .map(option -> option.getProductOptionValue().getId())
                .sorted()  // 비교를 위해 정렬
                .toList();

        List<Long> inputOptionIds = (optionValueIds == null ? List.of() : optionValueIds.stream().sorted().toList());
        return existingOptionIds.equals(inputOptionIds);
    }

    /**
     * 장바구니 항목 수량 업데이트
     */
    public void updateCartItemQuantity(Long customerId, Long cartItemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목입니다."));

        Cart cart = getOrCreateCart(customerId);
        cartItem.updateQuantity(newQuantity);

        cartItem.finalUnitPrice();
        cart.finalTotalPrice();

        cartItemRepository.save(cartItem);
    }

    /**
     * 장바구니 항목 삭제
     */
    public void removeCartItem(Long customerId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목입니다."));

        Cart cart = getOrCreateCart(customerId);
        cart.removeCartItem(cartItem);
        cart.finalTotalPrice();

        cartItemRepository.deleteById(cartItemId);
    }
}
