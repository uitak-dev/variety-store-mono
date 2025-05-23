package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.seller.converter.ProductAttributeConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category_id", nullable = false)
    private Category primaryCategory;   // 판매자가 상품 등록 시, 선택한 카테고리

    @Column(nullable = false)
    private String name;

    /**
     * 상품에 매핑된 이미지 컬렉션.
     * 하나의 이미지에 isThumbnail=true 로 설정하면 썸네일로 사용.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn
    private final List<ProductImage> productImages = new ArrayList<>();

    @Column(nullable = false)
    private String description; // 상품 설명

    @Column(nullable = false)
    private BigDecimal basePrice;   // 기본 가격

    @Column(nullable = false)
    private LocalDate manufactureDate; // 제조일자

    private int stockQuantity;  // 기본 재고 (옵션이 없을 경우 사용)
    private boolean single;     // true: 단일 상품, false: 옵션이 있는 상품.

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Seller seller;    // 판매자

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();    // 상품 카테고리

    @OneToMany(mappedBy ="product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<ProductOption> productOptions = new LinkedHashSet<>();    // 상품 옵션

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.PENDING; // 상품 등록 상태(기본값 PENDING)

    @Convert(converter = ProductAttributeConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> attributes; // 상품별 특화 속성

    @Builder
    public Product(Long id, Category primaryCategory, String name, String description, Boolean single,
                   BigDecimal basePrice, LocalDate manufactureDate, int stockQuantity, Seller seller,
                   Map<String, Object> attributes) {

        this.id = id;
        this.primaryCategory = primaryCategory;
        this.name = name;
        this.description = description;
        this.single = single;
        this.basePrice = basePrice;
        this.manufactureDate = manufactureDate;
        this.stockQuantity = stockQuantity;
        this.attributes = attributes;
        setSeller(seller);
    }

    /**
     * association convenience method
     */
    // 상품-옵션 연관관계 편의 메서드.
    public void addProductOption(ProductOption productOption) {
        productOptions.add(productOption);
        productOption.assignProduct(this);
    }

    // 상품-판매자 양방향 연관관계
    private void setSeller(Seller seller) {
        this.seller = seller;
        seller.getProducts().add(this);
    }

    // 해당 상품이 포함된 카테고리 목록
    public List<Category> getCategories() {
        return productCategories.stream()
                .map(ProductCategory::getCategory)
                .toList();
    }

    // 상품 이미지 추가.
    public void addImage(ProductImage image) {
        productImages.add(image);
        image.assignProduct(this);
    }

    // 상품 이미지 삭제.
    public void removeImage(ProductImage image) {
        productImages.remove(image);
        image.assignProduct(null);
    }

    // 상품 썸네일 이미지 조회.
    public ProductImage getThumbnail() {
        return productImages.stream()
                .filter(ProductImage::isThumbnail)
                .findFirst()
                .orElse(null);
    }

    // 상품 이미지 조회.(썸네일 이미지 제외)
    public List<ProductImage> getImages() {
        return productImages.stream()
                .filter(productImage -> !productImage.isThumbnail())
                .toList();
    }

    // 상품 썸네일 이미지 수정.
    public void setThumbnail(ProductImage newThumbnail) {
        Optional<ProductImage> findThumbnail = productImages.stream()
                .filter(ProductImage::isThumbnail)
                .findFirst();

        if (findThumbnail.isPresent()) {
            ProductImage productImage = findThumbnail.get();
            removeImage(productImage);
        }

        newThumbnail.setThumbnail(true);
        addImage(newThumbnail);
    }

    /**
     * 상품 기본 정보 수정
     */
    public void updateProduct(Category primaryCategory, String name, String description,
                              boolean single, BigDecimal basePrice, LocalDate manufactureDate,
                              int stockQuantity, Map<String, Object> attributes) {
        this.primaryCategory = primaryCategory;
        this.name = name;
        this.description = description;
        this.single = single;
        this.basePrice = basePrice;
        this.manufactureDate = manufactureDate;
        this.stockQuantity = stockQuantity;
        this.attributes = attributes;

        this.status = ProductStatus.PENDING;
    }

    /**
     * 상품 상태 변경 메서드
     */
    // 상품 승인: 등록 대기 상태(PENDING)인 경우, 승인(APPROVED) 상태로 전환 가능.
    public void approve() {
        if (this.status != ProductStatus.PENDING) {
            throw new IllegalStateException("등록 대기 상태인 경우에만, 승인 가능.");
        }
        this.status = ProductStatus.APPROVED;
    }

    // 상품 반려: 등록 대기 상태(PENDING)인 경우, 반려(REJECTED) 상태로 전환 가능.
    public void reject() {
        if (this.status != ProductStatus.PENDING) {
            throw new IllegalStateException("등록 대기 상태인 경우에만, 반려 가능.");
        }
        this.status = ProductStatus.REJECTED;
    }

    // 판매 중단: 상품이 승인(APPROVED) 상태일 때만 중단(DISCONTINUED) 가능
    public void discontinue() {
        if (this.status != ProductStatus.APPROVED) {
            throw new IllegalStateException("승인 상태인 경우에만, 중단 가능.");
        }
        this.status = ProductStatus.DISCONTINUED;
    }

    // 품절: 재고 수량과 상관없이, 판매자가 제품 손상/불량 등의 이유로 품절 상태로 전환 가능.
    public void markOutOfStock() {
        if (this.status != ProductStatus.APPROVED) {
            throw new IllegalStateException("승인 상태인 경우에만, 품절 가능.");
        }
        this.status = ProductStatus.OUT_OF_STOCK;
    }

    /** 상품 재고 관리 메서드 */
    public void increaseInventory(int count) {
        this.stockQuantity += count;
    }

    public void decreaseInventory(int count) {
        if (stockQuantity < count) {
            throw new IllegalStateException("재고량 부족.");
        }
        this.stockQuantity -= count;
    }
}
