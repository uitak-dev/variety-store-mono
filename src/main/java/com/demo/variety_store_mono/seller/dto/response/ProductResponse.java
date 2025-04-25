package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    // 판매자가 상품 등록 시, 설정한 카테고리
    private CategorySummary primaryCategory;

    // 상품 기본 정보
    private String name;
    private String description;
    private BigDecimal basePrice;

    // 상품 이미지(파일 업로드 후, 받은 저장된 파일명; storeFileName)
    private UploadFileResponse thumbnail;
    private List<UploadFileResponse> images;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufactureDate;

    private int stockQuantity;
    private boolean single;
    private ProductStatus status;

    private List<ProductOptionResponse> productOptions;
}
