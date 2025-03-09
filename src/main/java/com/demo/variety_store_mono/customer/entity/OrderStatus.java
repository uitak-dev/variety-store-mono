package com.demo.variety_store_mono.customer.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING,            // 결제 대기
    PAID,               // 결제 완료
    SHIPPED,            // 출고됨
    DELIVERED,          // 배송 완료
    RETURN_REQUESTED,   // 반품 요청
    RETURNED,           // 반품 완료
    CANCELED            // 주문 취소
}
