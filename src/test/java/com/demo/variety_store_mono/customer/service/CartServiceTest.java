package com.demo.variety_store_mono.customer.service;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.config.modelmapper.ModelMapperConfig;
import com.demo.variety_store_mono.customer.dto.response.CartResponse;
import com.demo.variety_store_mono.customer.entity.Cart;
import com.demo.variety_store_mono.customer.entity.CartItem;
import com.demo.variety_store_mono.customer.entity.CartItemOption;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.customer.repository.CartItemRepository;
import com.demo.variety_store_mono.customer.repository.CartRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.Seller;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.test_data_builder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    // 더미 엔티티들
    private User dummyUserCustomer;
    private Customer dummyCustomer;
    private User dummyUserSeller;
    private Seller dummySeller;
    private Cart dummyCart;
    private Product dummyProduct;
    private ProductOptionValue largeSize;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        cartService = new CartService(cartRepository, cartItemRepository,
                userRepository, productRepository, modelMapper);

        dummyUserCustomer = new UserTestDataBuilder(UserType.CUSTOMER).withId(1L).build();
        dummyCustomer = dummyUserCustomer.createCustomerDetail();
        ReflectionTestUtils.setField(dummyCustomer, "id", 1L);
        dummyCart = new Cart(dummyCustomer);

        dummyUserSeller = new UserTestDataBuilder(UserType.SELLER).withId(11L).build();
        dummySeller = dummyUserSeller.createSellerDetail();
        ReflectionTestUtils.setField(dummySeller, "id", 11L);

        largeSize = new ProductOptionValueTestDataBuilder()
                .withId(301L)
                .withOptionValue("Large")
                .build();

        ProductOptionValue smallSize = new ProductOptionValueTestDataBuilder()
                .withId(302L)
                .withOptionValue("Small")
                .build();

        ProductOption sizeOption = new ProductOptionTestDataBuilder()
                .withId(201L)
                .withName("사이즈")
                .withProductOptionValues(Set.of(largeSize, smallSize))
                .build();

        dummyProduct = new ProductTestDataBuilder()
                .withId(101L)
                .withSingle(false)
                .withProductOption(sizeOption)
                .withSeller(dummySeller)
                .build();
    }

    @Test
    void testGetOrCreateCart_WhenCartExists() {
        // Given: 고객 존재 설정
        Long customerId = 1L;
        when(userRepository.findById(customerId)).thenReturn(Optional.of(dummyUserCustomer));
        // 이미 존재하는 장바구니를 반환하도록 설정
        ReflectionTestUtils.setField(dummyCart, "id", 1L);
        when(cartRepository.findByCustomerId(dummyCustomer.getId())).thenReturn(Optional.of(dummyCart));

        // When
        CartResponse cartResponse = cartService.getCartDetails(customerId);

        // Then
        assertNotNull(cartResponse, "반환된 cartResponse 는 null 이 아니어야 합니다.");
        assertEquals(dummyCart.getId(), cartResponse.getId(), "기존의 Cart 가 반환되어야 합니다.");
        verify(userRepository, times(1)).findById(customerId);
        verify(cartRepository, times(1)).findByCustomerId(dummyCustomer.getId());
        verify(cartRepository, never()).save(any(Cart.class));  // save()는 호출되지 않아야 함 (이미 존재하므로)
    }

    @Test
    void testGetOrCreateCart_WhenCartDoesNotExist() {
        // Given: Dummy
        Long customerId = 2L;
        // 새로운 고객 객체 생성.
        User newUser = new UserTestDataBuilder(UserType.CUSTOMER)
                .withId(customerId)
                .withUserName("New Test Customer")
                .build();
        Customer newCustomer = newUser.createCustomerDetail();
        ReflectionTestUtils.setField(newCustomer, "id", customerId);

        // Given: Stub
        when(userRepository.findById(customerId)).thenReturn(Optional.of(newUser));
        // 장바구니 존재하지 않음.
        when(cartRepository.findByCustomerId(newCustomer.getId())).thenReturn(Optional.empty());

        // 대상 메서드에서 생성할 Cart 정의.
        Cart newCart = new Cart(newCustomer);
        ReflectionTestUtils.setField(newCart, "id", 2L);
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // When
        CartResponse cartResponse = cartService.getCartDetails(customerId);

        // Then: Spy
        assertNotNull(cartResponse, "새로 생성된 Cart는 null이 아니어야 합니다.");
        assertEquals(newCustomer.getCart().getId(), cartResponse.getId(),
                "newCustomer의 Cart가 올바르게 설정되어야 합니다.");
        verify(userRepository, times(1)).findById(customerId);
        verify(cartRepository, times(1)).findByCustomerId(customerId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_NewItem() {
        // Given: Dummy
        Long customerId = 1L;
        Long productId = 101L;
        int quantityToAdd = 2;
        List<Long> optionValueIds = List.of(301L);

        // Given: Stub
        // 고객과 장바구니가 존재하는 상태
        when(userRepository.findById(customerId)).thenReturn(Optional.of(dummyUserCustomer));
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(dummyCart));

        // 상품 존재
        when(productRepository.findById(productId)).thenReturn(Optional.of(dummyProduct));
        // 옵션 값 존재
        when(productRepository.findProductOptionValueById(301L)).thenReturn(Optional.of(largeSize));

        // 장바구니에 모든 항목을 비움.
        dummyCart.getCartItems().clear();

        // When: 상품 추가.
        cartService.addItemToCart(customerId, productId, quantityToAdd, optionValueIds);

        // Then: Spy
        // 장바구니에 상품이 추가되었는지 확인 (CartItem)
        assertFalse(dummyCart.getCartItems().isEmpty(), "장바구니에 CartItem이 추가되어야 합니다.");
        CartItem addedItem = dummyCart.getCartItems().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("장바구니에 추가된 상품이 존재하지 않습니다."));
        assertEquals(dummyProduct, addedItem.getProduct(), "추가된 CartItem의 상품은 올바르게 매핑되어야 합니다.");
        assertEquals(quantityToAdd, addedItem.getQuantity(), "CartItem의 수량이 올바르게 설정되어야 합니다.");

        // 장바구니에 추가된 상품에 옵션이 추가되었는지 확인 (CartItemOption)
        assertFalse(addedItem.getCartItemOptions().isEmpty(), "CartItem에 옵션 항목이 추가되어야 합니다.");
        CartItemOption itemOption = addedItem.getCartItemOptions().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("장바구니에 추가된 상품의 옵션이 존재하지 않습니다."));
        assertEquals(largeSize, itemOption.getProductOptionValue(), "CartItemOption의 옵션 값이 올바르게 매핑되어야 합니다.");

        verify(cartRepository, atLeastOnce()).findByCustomerId(dummyCustomer.getId());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findProductOptionValueById(301L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddItemToCart_ExistingItem_IncreaseQuantity() {
        // Given
        Long customerId = 1L;
        Long productId = 101L;
        int initialQuantity = 2;
        int additionalQuantity = 3;
        List<Long> optionValueIds = List.of(301L);

        // 고객 및 장바구니 존재
        when(userRepository.findById(customerId)).thenReturn(Optional.of(dummyUserCustomer));
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(dummyCart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(dummyProduct));

        // 기존 CartItem 생성 및 연관관계 설정.
        CartItem existingItem = new CartItem(dummyCart, dummyProduct, initialQuantity);
        CartItemOption existingOption = new CartItemOption(largeSize);
        existingItem.addCartItemOption(existingOption);

        // 기존 항목을 Cart에 추가
        dummyCart.getCartItems().clear();
        dummyCart.addCartItem(existingItem);

        // When: 기존 항목과 동일한 상품 및 옵션으로 추가 요청 (추가 수량)
        cartService.addItemToCart(customerId, productId, additionalQuantity, optionValueIds);

        // Then: 기존 CartItem의 수량이 증가하여 initialQuantity + additionalQuantity가 되어야 함.
        int expectedQuantity = initialQuantity + additionalQuantity;
        assertEquals(expectedQuantity, existingItem.getQuantity(), "CartItem의 수량이 누적되어 증가해야 합니다.");
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void testUpdateCartItemQuantity() {
        // Given
        Long cartItemId = 10L;
        int initialQuantity = 3;
        int newQuantity = 5;

        CartItem existingItem = new CartItem(dummyCart, dummyProduct, initialQuantity);
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingItem));

        // When: 수량 업데이트 요청
        cartService.updateCartItemQuantity(cartItemId, newQuantity);

        // Then: CartItem의 수량이 새 값으로 업데이트
        assertEquals(newQuantity, existingItem.getQuantity(), "CartItem의 수량이 업데이트되어야 합니다.");
        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void testRemoveCartItem() {
        // Given
        Long cartItemId = 20L;
        when(cartItemRepository.existsById(cartItemId)).thenReturn(true);

        // When: CartItem 삭제 요청
        cartService.removeCartItem(cartItemId);

        // Then: existsById와 deleteById가 각각 호출되어야 함
        verify(cartItemRepository, times(1)).existsById(cartItemId);
        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }

    @Test
    void testGetCartDetails() {
        // Given
        Long customerId = 1L;
        when(userRepository.findById(customerId)).thenReturn(Optional.of(dummyUserCustomer));
        // 이미 존재하는 장바구니를 반환하도록 설정
        ReflectionTestUtils.setField(dummyCart, "id", 1L);
        when(cartRepository.findByCustomerId(dummyCustomer.getId())).thenReturn(Optional.of(dummyCart));

        // When: 장바구니 내역 조회 요청
        CartResponse cartResponse = cartService.getCartDetails(customerId);

        // Then: 기존 Cart가 반환되어야 함
        assertNotNull(cartResponse, "장바구니 조회 결과는 null이 아니어야 합니다.");
        verify(userRepository, times(1)).findById(customerId);
        verify(cartRepository, times(1)).findByCustomerId(dummyCustomer.getId());
    }
}