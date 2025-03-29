package com.demo.variety_store_mono.seller.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    PENDING("등록 대기", "PENDING"),
    APPROVED("승인", "APPROVED"),
    REJECTED("반려", "REJECTED"),
    DISCONTINUED("판매 중단", "DISCONTINUED"),
    OUT_OF_STOCK("품절", "OUT_OF_STOCK");

    // 사용자에게 노출할 이름
    private final String displayName;

    // 시스템에서 사용할 값 (원하는 타입으로 변경 가능)
    private final String systemValue;
}
