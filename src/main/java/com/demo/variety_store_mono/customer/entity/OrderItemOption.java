package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOption {

    @Id
    @GeneratedValue
    @Column(name = "order_item_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_value_id", nullable = true)
    private ProductOptionValue productOptionValue;

    // 주문 시점의 옵션명을 스냅샷으로 저장 (데이터 안정성 확보)
    @Column(nullable = false)
    private String optionName;

    // 주문 시점의 옵션 값을 스냅샷으로 저장 (데이터 안정성 확보)
    @Column(nullable = false)
    private String optionValue;

    // 해당 옵션 선택에 따른 추가 가격 (없으면 0)
    @Column(nullable = false)
    private BigDecimal additionalPrice = BigDecimal.ZERO;

    // 옵션의 출처를 구분
//    @Column(nullable = false)
//    private boolean isGlobalOption; // (false = ProductOption)

    @Builder
    public OrderItemOption(ProductOptionValue productOptionValue, String optionName,
                           String optionValue, BigDecimal additionalPrice) {
        this.productOptionValue = productOptionValue;
        this.optionName = optionName;
        this.optionValue = optionValue;
        this.additionalPrice = additionalPrice;
    }

    void assignOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
