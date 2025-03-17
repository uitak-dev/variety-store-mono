package com.demo.variety_store_mono.seller.service;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.request.SearchCategory;
import com.demo.variety_store_mono.admin.response.CategoryResponse;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.seller.request.ProductOptionRequest;
import com.demo.variety_store_mono.seller.request.ProductRequest;
import com.demo.variety_store_mono.seller.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    /** 상품 등록 */
    public ProductResponse createProduct(ProductRequest request) {

        User seller = userRepository.findById(request.getSellerId()).orElseThrow(() ->
                new EntityNotFoundException("등록된 판매자가 아닙니다."));

        Category category = categoryRepository.findById(request.getProductCategoryId()).orElseThrow(() ->
                new EntityNotFoundException("등록되지 않은 카테고리 입니다."));

        List<ProductOptionRequest> productOptions = request.getProductOptions();
        for (ProductOptionRequest option : productOptions) {
            if (option.isGlobal()) {
                Long globalOptionId = option.getGlobalOptionId();

            }
            else {

            }
        }

        return null;
    }

    /** 상품 목록 조회 */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductSearchList(SearchCategory searchCategory, Pageable pageable) {

        return null;
    }

    /** 상품 상세 조회 */
    @Transactional(readOnly = true)
    public CategoryResponse getProduct(Long productId) {

        return null;
    }

    /** 상품 수정 */
    public CategoryResponse updateProduct(Long productId, ProductRequest request) {

        return null;
    }

    /** 상품 삭제 */
    public void deleteCategory(Long productId) {


    }
}
