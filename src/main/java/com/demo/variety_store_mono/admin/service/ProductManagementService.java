package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.dto.response.ProductManagementResponse;
import com.demo.variety_store_mono.admin.dto.search.SearchProductManagement;
import com.demo.variety_store_mono.admin.dto.summary.ProductManagementSummary;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import com.demo.variety_store_mono.config.modelmapper.SellerToSellerDetailResponse;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Destination;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductManagementService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    /** 상품 목록 조회 */
    @Transactional(readOnly = true)
    public Page<ProductManagementSummary> getProductSearchList(SearchProductManagement searchProduct, Pageable pageable) {
        return productRepository.searchProductManagementList(searchProduct, pageable);
    }

    // 상품 상세 조회.
    @Transactional(readOnly = true)
    public ProductManagementResponse getProductDetails(Long productId) {

        Product product = productRepository.findProductManagementDetails(productId)
                .orElseThrow(() -> new EntityNotFoundException("관련 상품을 찾을 수 없습니다."));

        return modelMapper.map(product, ProductManagementResponse.class);
    }

    // 등록 상태 변경.
}
