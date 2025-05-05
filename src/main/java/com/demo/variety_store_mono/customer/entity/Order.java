package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.security.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer; // 주문한 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING; // 주문 상태 (기본: 결제 대기)

    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO; // 주문 총 금액

    @Column(nullable = false)
    private LocalDateTime orderDate; // 주문 일시

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @Builder
    public Order(Customer customer, LocalDateTime orderDate) {
        this.customer = customer;
        this.orderDate = orderDate;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public BigDecimal finalTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            // 각 주문 항목의 총액 = 단가 * 수량
            BigDecimal itemTotal = orderItem.finalUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            total = total.add(itemTotal);
        }
        this.totalPrice = total;
        return total;
    }

}
