package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.customer.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    private List<OrderItemResponse> orderItems;
}
