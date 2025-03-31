package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.admin.dto.response.CategoryResponse;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    // 상품 기본 정보
    private List<CategoryResponse> categories;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private LocalDate manufactureDate;
    private int stockQuantity;
    private boolean single;
    private ProductStatus status;

    private List<ProductOptionResponse> productOptions;
}
