package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionValue {

    @Id
    @GeneratedValue
    @Column(name = "product_option_value_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_option_value_id", nullable = true)
    private GlobalOptionValue globalOptionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    private String optionValue;   // 판매자 정의 옵션 값 (예: "빨강", "16GB", "512GB")
    private boolean global;       // true: 글로벌 옵션 값, false: 사용자 정의 옵션 값

    @Column(nullable = false)
    private BigDecimal additionalPrice; // 옵션 선택 시 추가 가격

    @Column
    private int stockQuantity; // 옵션별 재고

    // 재고 수량과 별도로, 품절 여부를 판매자가 수동으로 제어하거나 예약 주문 등
    // 특별한 상황(품질 문제, 판매 중단)에서 품절 상태를 표시할 필요가 있는 경우.
    @Column(nullable = false)
    private boolean outOfStock = false; // 옵션 품절 여부

    @Builder
    public ProductOptionValue(Long id, String optionValue, boolean global,
                              BigDecimal additionalPrice, int stockQuantity) {
        this.id = id;
        this.global = global;
        this.optionValue = optionValue;
        this.additionalPrice = additionalPrice;
        this.stockQuantity = stockQuantity;
    }

    /** * * * * * * * * * * * * * * * *
     * association convenience method *
     * * * * * * * * * * * * * * * * */
    void assignProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    public void assignGlobalOptionValue(GlobalOptionValue globalOptionValue) {
        this.globalOptionValue = globalOptionValue;
    }
}
