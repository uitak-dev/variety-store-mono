package com.demo.variety_store_mono.customer.service;

import com.demo.variety_store_mono.customer.dto.response.ProductCatalogResponse;
import com.demo.variety_store_mono.customer.dto.summary.ProductCatalogSummary;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.utility.mapper.ProductCatalogMapper;
import com.demo.variety_store_mono.utility.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCatalogService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    /* 주어진 카테고리와 해당 카테고리의 모든 하위 카테고리에 포함된 상품 목록 조회. */
    public Page<ProductCatalogSummary> getProductCatalogList(Long categoryId, Pageable pageable) {
        return productRepository.findProductsByCategory(categoryId, pageable);
    }

    /* 상품 상세 조회. */
    public ProductCatalogResponse getProductCatalogDetails(Long productId) {
        Product product = productRepository.findProductCatalogDetails(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return ProductCatalogMapper.toResponse(product);
    }
}
