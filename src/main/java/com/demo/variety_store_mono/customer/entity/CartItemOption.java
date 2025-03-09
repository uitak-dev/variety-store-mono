package com.demo.variety_store_mono.customer.entity;


import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    // 시스템에 등록된 옵션 값인 경우 GlobalOptionValue를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_option_value_id", nullable = true)
    private GlobalOptionValue globalOptionValue;

    // 판매자 커스텀 옵션 값인 경우 ProductOptionValue를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_value_id", nullable = true)
    private ProductOptionValue productOptionValue;
}

