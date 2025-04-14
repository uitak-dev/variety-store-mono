package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.customer.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;
    private List<CartItemResponse> cartItems;
    private Long customerId;
}
