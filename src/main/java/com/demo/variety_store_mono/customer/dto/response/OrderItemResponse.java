package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private ProductSummary product;
    private int quantity;
    private BigDecimal unitPirce;   // 결제 금액(옵션 포함)

    private List<OrderItemOptionResponse> orderItemOptions;
}
