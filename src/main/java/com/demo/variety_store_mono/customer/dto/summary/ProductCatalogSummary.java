package com.demo.variety_store_mono.customer.dto.summary;

import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCatalogSummary {

    private Long id;
    private String name;

    private UploadFileResponse thumbnail;

    private String description;
    private BigDecimal basePrice;
    private SellerDetailResponse seller;
}
