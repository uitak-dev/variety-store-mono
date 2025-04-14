package com.demo.variety_store_mono.customer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private Long productId;
    private int quantity;
    private Set<Long> optionValueIds;
}
