package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.dto.response.ProductOptionResponse;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCatalogResponse {

    private Long id;

    private CategorySummary primaryCategory;

    private String name;
    private String description;
    private BigDecimal basePrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufactureDate;

    // 상품 이미지(파일 업로드 후, 받은 저장된 파일명; storeFileName)
    private UploadFileResponse thumbnail;
    private List<UploadFileResponse> images;

    private boolean single;
    private List<ProductOptionResponse> productOptions;
    private ProductStatus status;

    private SellerDetailResponse seller;
}
