package com.demo.variety_store_mono.seller.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {

    PENDING,        // 등록 대기
    APPROVED,       // 승인 (사용자에게 노출 가능)
    REJECTED,       // 반려
    DISCONTINUED,   // 판매 중단
    OUT_OF_STOCK    // 품절
}
