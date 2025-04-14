package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private ProductSummary product;
    private int quantity;
    private BigDecimal unitPirce;   // 결제 금액(옵션 포함)

    // 주문 시 선택한 옵션, 옵션 값 정보 추가.
}
