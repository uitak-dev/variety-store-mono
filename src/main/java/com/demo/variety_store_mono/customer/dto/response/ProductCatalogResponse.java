package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.dto.response.ProductOptionResponse;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalogResponse {

    private Long id;
    private CategorySummary primaryCategory;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private LocalDate manufactureDate;
    private boolean single;
    private SellerDetailResponse seller;
    private Set<ProductOptionResponse> productOptions;
    private ProductStatus status;
}
