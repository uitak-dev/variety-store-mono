package com.demo.variety_store_mono.admin.dto.search;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductManagement {

    String name;    // 상품 이름
    ProductStatus status;
    String userName;    // 판매자 아이디
    Long categoryId;
}
