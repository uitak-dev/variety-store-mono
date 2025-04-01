package com.demo.variety_store_mono.admin.dto.response;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.dto.response.ProductOptionResponse;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductManagementResponse {

    private Long id;

    // 상품 기본 정보
    private List<CategorySummary> categories;

    private String name;
    private String description;
    private BigDecimal basePrice;
    private LocalDate manufactureDate;
    private int stockQuantity;
    private boolean single;
    private ProductStatus status;

    // 상품 옵션 정보
    private List<ProductOptionResponse> productOptions;

    // 판매자 정보
    private SellerDetailResponse seller;
}
