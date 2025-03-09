package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 주문한 상품

    @ManyToOne
    @JoinColumn(name = "option_value_id")
    private ProductOptionValue productOptionValue; // 선택한 옵션 값

    @Column(nullable = false)
    private int quantity; // 주문 수량

    @Column(nullable = false)
    private BigDecimal unitPrice; // 개별 상품의 주문 가격 (옵션 포함)
}
