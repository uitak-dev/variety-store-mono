package com.demo.variety_store_mono.admin.dto.summary;

import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductManagementSummary {

    private Long id;

    // 상품 기본 정보
    private List<CategorySummary> categories;
    private String name;
    private ProductStatus status;

    // 판매자 정보(아이디)
    private String userName;
}
