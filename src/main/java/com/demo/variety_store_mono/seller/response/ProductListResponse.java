package com.demo.variety_store_mono.seller.response;

import com.demo.variety_store_mono.admin.response.CategoryResponse;
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
public class ProductListResponse {

    private Long id;

    // 상품 기본 정보
    private List<CategoryResponse> categories;
    private String name;
    private BigDecimal basePrice;
    private int stockQuantity;
    private ProductStatus status;
}
