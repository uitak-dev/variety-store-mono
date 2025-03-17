package com.demo.variety_store_mono.seller.request;

import java.math.BigDecimal;

public class ProductOptionValueRequest {

    // 시스템에 등록된 옵션 값 선택 시
    private Long globalOptionValueId;

    // 사용자 정의 옵션 값 사용 시
    private String productOptionValue;

    // 해당 옵션 선택 시 추가되는 가격
    private BigDecimal additionalPrice;

    // 해당 옵션 선택 시 재고 수량
    private int stockQuantity;
}
