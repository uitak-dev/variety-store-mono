package com.demo.variety_store_mono.seller.repository.custom;

import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {

    Page<ProductListResponse> searchProductList(Long sellerId, SearchProduct searchProduct, Pageable pageable);
}
