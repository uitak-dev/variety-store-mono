package com.demo.variety_store_mono.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemRequest {

    private Long productId;
    private int quantity;
    private List<Long> optionValueIds;
}