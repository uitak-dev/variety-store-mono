package com.demo.variety_store_mono.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemOptionResponse {

    private String optionName;
    private String optionValue;
    private BigDecimal additionalPrice;
}
