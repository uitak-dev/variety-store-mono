package com.demo.variety_store_mono.seller.dto.summary;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummary {

    private Long id;

    // 상품 기본 정보
    private List<CategorySummary> categories;
    private String name;
    private BigDecimal basePrice;
    private int stockQuantity;
    private ProductStatus status;
}
