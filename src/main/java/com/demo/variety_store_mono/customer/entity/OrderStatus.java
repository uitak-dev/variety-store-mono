package com.demo.variety_store_mono.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PENDING("결제 대기", "PENDING"),
    PAID("결제 완료", "PAID"),
    SHIPPED("출고됨", "SHIPPED"),
    DELIVERED("배송 완료", "DELIVERED"),
    RETURN_REQUESTED("반품 요청", "RETURN_REQUESTED"),
    RETURNED("반품 완료", "RETURNED"),
    CANCELED("주문 취소", "CANCELED");

    // 사용자에게 노출할 이름
    private final String displayName;

    // 시스템에서 사용할 값 (원하는 타입으로 변경 가능)
    private final String systemValue;
}
