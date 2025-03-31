package com.demo.variety_store_mono.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionValueRequest {

    private boolean global;

    // 시스템에 등록된 옵션 값 선택 시
    private Long globalOptionValueId;

    // 사용자 정의 옵션 값 사용 시
    private String optionValue;

    // 해당 옵션 선택 시 추가되는 가격
    private BigDecimal additionalPrice;

    // 해당 옵션 선택 시 재고 수량
    private int stockQuantity;
}
