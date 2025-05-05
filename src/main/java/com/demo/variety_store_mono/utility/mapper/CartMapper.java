package com.demo.variety_store_mono.utility.mapper;

import com.demo.variety_store_mono.customer.dto.response.CartItemOptionResponse;
import com.demo.variety_store_mono.customer.dto.response.CartItemResponse;
import com.demo.variety_store_mono.customer.dto.response.CartResponse;
import com.demo.variety_store_mono.customer.dto.summary.ProductCatalogSummary;
import com.demo.variety_store_mono.customer.entity.Cart;
import com.demo.variety_store_mono.customer.entity.CartItem;
import com.demo.variety_store_mono.customer.entity.CartItemOption;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class CartMapper {

    private static final ModelMapper basicMapper = new ModelMapper();

    public static CartResponse toResponse(Cart cart) {

        Long id = cart.getId();
        BigDecimal totalPrice = cart.getTotalPrice();
        List<CartItemResponse> list = cart.getCartItems().stream()
                .map(CartMapper::makeCartItemResponse)
                .toList();

        return new CartResponse(id, list, totalPrice);
    }

    private static CartItemResponse makeCartItemResponse(CartItem cartItem) {

        CartItemResponse ret = CartItemResponse.builder()
                .id(cartItem.getId())
                .product(ProductCatalogSummaryMapper.toResponse(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .build();

        List<CartItemOptionResponse> list = cartItem.getCartItemOptions().stream()
                .map(CartMapper::makeCartItemOptionResponse)
                .toList();

        ret.setCartItemOptions(list);
        return ret;
    }

    private static CartItemOptionResponse makeCartItemOptionResponse(CartItemOption cartItemOption) {
        ProductOptionValue productOptionValue = cartItemOption.getProductOptionValue();

        return CartItemOptionResponse.builder()
                .optionName(productOptionValue.getProductOption().getName())
                .optionValue(productOptionValue.getOptionValue())
                .additionalPrice(productOptionValue.getAdditionalPrice())
                .build();
    }
}
