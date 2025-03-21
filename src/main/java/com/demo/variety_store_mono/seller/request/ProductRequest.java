package com.demo.variety_store_mono.seller.request;

import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    // 상품 기본 정보
    private String name;
    private String description;
    private BigDecimal basePrice;
    private LocalDate manufactureDate;
    private int stockQuantity;

    // 판매자 id (인증된 사용자나 별도 값으로 처리)
    private Long sellerId;

    // 상품이 속할 카테고리 id
    private Long productCategoryId;

    // 상품 옵션 정보 (상품 옵션 및 옵션 값에 대한 요청 DTO를 별도로 정의)
    private List<ProductOptionRequest> productOptions;

    // 상품 등록 상태 (문자열이나 enum 타입으로 처리할 수 있음)
    private ProductStatus status;

    // 상품별 특화 속성
    private Map<String, Object> attributes;
}
