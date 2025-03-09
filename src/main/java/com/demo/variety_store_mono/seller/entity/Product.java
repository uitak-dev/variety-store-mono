package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.seller.converter.ProductAttributeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description; // 상품 설명

    @Column(nullable = false)
    private BigDecimal basePrice;   // 기본 가격

    @Column(nullable = false)
    LocalDate manufactureDate; // 제조일자

    @Column
    private int stockQuantity; // 기본 재고 (옵션이 없을 경우 사용)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;    // 판매자

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories = new ArrayList<>();    // 상품 카테고리

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>(); // 상품 옵션

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.PENDING; // 상품 등록 상태(기본값 PENDING)

    @Convert(converter = ProductAttributeConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> attributes; // 상품별 특화 속성
}
