package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    // 시스템 옵션일 경우 GlobalOptionValue를, 판매자 커스텀 옵션일 경우 ProductOptionValue를 참조할 수 있도록 함.
    // 두 참조를 동시에 두는 대신, 옵션 출처에 따라 하나의 값을 채우거나, 스냅샷으로 처리할 수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_option_value_id", nullable = true)
    private GlobalOptionValue globalOptionValue;

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
    @Column(nullable = false)
    private boolean isGlobalOption; // (false = ProductOption)
}
