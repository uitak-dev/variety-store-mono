package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false)
    private int quantity; // 주문 수량

    @Column(nullable = false)
    private BigDecimal unitPrice; // 상품의 주문 가격( 옵션 포함 )

    // 다중 옵션 선택을 위한 연관관계
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemOption> orderItemOptions = new LinkedHashSet<>();

    @Builder
    public OrderItem(Product product, int quantity, BigDecimal unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    public void addOrderItemOption(OrderItemOption orderItemOption) {
        orderItemOptions.add(orderItemOption);
        orderItemOption.assignOrderItem(this);
    }

    public BigDecimal finalUnitPrice() {
        // product와 orderItemOptions가 null이 아님이 보장된다는 전제하에 작성 (필수 제약조건)
        BigDecimal ret = product.getBasePrice();
        for (OrderItemOption option : orderItemOptions) {
            ret = ret.add(option.getAdditionalPrice());
        }
        unitPrice = ret;
        return ret;
    }
}
