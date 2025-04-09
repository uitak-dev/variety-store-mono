package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    // 판매자가 상품 등록 시, 설정한 카테고리
    private CategorySummary primaryCategory;

    // 상품 기본 정보
    private String name;
    private String description;
    private BigDecimal basePrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufactureDate;

    private int stockQuantity;
    private boolean single;
    private ProductStatus status;

    private List<ProductOptionResponse> productOptions;
}
