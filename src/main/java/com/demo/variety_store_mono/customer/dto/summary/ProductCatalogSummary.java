package com.demo.variety_store_mono.customer.dto.summary;

import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCatalogSummary {

    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private SellerDetailResponse seller;
}
