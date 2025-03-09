package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.admin.entity.GlobalOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_option_id", nullable = true)
    private GlobalOption globalOption;

    @Column
    private String name; // 판매자 정의 옵션 이름 (예: "색상", "RAM", "저장 용량")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // 상품과 연결
    private Product product;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionValue> productOptionValues = new ArrayList<>();
}
