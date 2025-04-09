package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.admin.dto.response.GlobalOptionValueResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionValueResponse {

    private boolean global;

    // 옵션 값
    private String productOptionValue;

    // 해당 옵션 선택 시 추가되는 가격
    private BigDecimal additionalPrice;

    // 해당 옵션 선택 시 재고 수량
    private int stockQuantity;
}
