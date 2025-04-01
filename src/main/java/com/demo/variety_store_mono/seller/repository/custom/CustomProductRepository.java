package com.demo.variety_store_mono.seller.repository.custom;

import com.demo.variety_store_mono.admin.dto.search.SearchProductManagement;
import com.demo.variety_store_mono.admin.dto.summary.ProductManagementSummary;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {

    // 판매자 도메인 - 상품 목록 조회.
    Page<ProductSummary> searchProductList(Long sellerId, SearchProduct searchProduct, Pageable pageable);

    // 관리자 도메인 - 상품 목록 조회.
    Page<ProductManagementSummary> searchProductManagementList(SearchProductManagement searchProduct, Pageable pageable);
}
