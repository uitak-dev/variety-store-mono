package com.demo.variety_store_mono.seller.response;

import com.demo.variety_store_mono.admin.response.GlobalOptionValueResponse;
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

    // true: 시스템에 정의된 옵션 값 선택
    // false: 사용자 정의 옵션 값 사용
    private boolean global;

    // 시스템에 등록된 옵션 값 선택 시
    private GlobalOptionValueResponse  globalOptionValue;

    // 사용자 정의 옵션 값 사용 시
    private String productOptionValue;

    // 해당 옵션 선택 시 추가되는 가격
    private BigDecimal additionalPrice;

    // 해당 옵션 선택 시 재고 수량
    private int stockQuantity;
}
