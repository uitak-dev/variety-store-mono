package com.demo.variety_store_mono.seller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    // 상품 기본 정보
    private String name;
    private String description;
    private BigDecimal basePrice;
    private LocalDate manufactureDate;
    private int stockQuantity;


}
