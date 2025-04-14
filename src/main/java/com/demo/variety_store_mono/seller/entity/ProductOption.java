package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.admin.entity.GlobalOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private boolean global;     // true: 글로벌 옵션, false: 사용자 정의 옵션.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_option_id", nullable = true)
    private GlobalOption globalOption;

    private String name;        // 판매자 정의 옵션 이름 (예: "색상", "RAM", "저장 용량")

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductOptionValue> productOptionValues = new LinkedHashSet<>();

    @Builder
    public ProductOption(Long id, GlobalOption globalOption, boolean global,
                         String name) {
        this.id = id;
        this.globalOption = globalOption;
        this.global = global;
        this.name = name;
    }

    /** association convenience method */
    void assignProduct(Product product) {
        this.product = product;
    }

    public void assignGlobalOption(GlobalOption globalOption) {
        this.globalOption = globalOption;
    }

    public void addProductOptionValue(ProductOptionValue productOptionValue) {
        productOptionValues.add(productOptionValue);
        productOptionValue.assignProductOption(this);
    }
}
