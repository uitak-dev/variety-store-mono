package com.demo.variety_store_mono.seller.service;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.seller.dto.request.ProductOptionRequest;
import com.demo.variety_store_mono.seller.dto.request.ProductOptionValueRequest;
import com.demo.variety_store_mono.seller.dto.request.ProductRequest;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductListResponse;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GlobalOptionRepository globalOptionRepository;

    private final ModelMapper modelMapper;

    /** 상품 등록 */
    public ProductResponse createProduct(Long sellerId, ProductRequest request) {

        User user = userRepository.findUserDetailsById(sellerId).orElseThrow(() ->
                new EntityNotFoundException("등록된 판매자가 아닙니다."));

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException("등록되지 않은 카테고리 입니다."));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .single(request.isSingle())
                .basePrice(request.getBasePrice())
                .stockQuantity(request.getStockQuantity())
                .manufactureDate(request.getManufactureDate())
                .attributes(request.getAttributes())
                .seller(user.getSeller())
                .build();

        // 옵션 상품인 경우, 옵션 등록.
        if (!request.isSingle() && request.getProductOptions() != null) {
            request.getProductOptions().forEach(optionRequest -> {
                ProductOption productOption = generateProductOption(optionRequest);
                product.addProductOption(productOption);
            });
        }

        Product savedProduct = productRepository.save(product);
        category.addProduct(product);

        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    /** 상품 목록 조회 */
    @Transactional(readOnly = true)
    public Page<ProductSummary> getProductSearchList(Long sellerId, SearchProduct searchProduct, Pageable pageable) {
        return productRepository.searchProductList(sellerId, searchProduct, pageable);
    }

    /** 상품 상세 조회 */
    @Transactional(readOnly = true)
    public ProductResponse getProductDetails(Long sellerId, Long productId) {

        Product product = productRepository.findProductDetails(sellerId, productId)
                .orElseThrow(() -> new EntityNotFoundException("관련 상품을 찾을 수 없습니다."));

        return modelMapper.map(product, ProductResponse.class);
    }

    /** 상품 수정 */
    public ProductResponse updateProduct(Long productId, ProductRequest request) {

        return null;
    }

    /** 상품 삭제 */
    public void deleteCategory(Long productId) {

    }

    private ProductOption generateProductOption(ProductOptionRequest request) {

        ProductOption productOption = ProductOption.builder()
                .global(request.isGlobal())
                .name(request.getName())
                .build();

        if (request.isGlobal() && request.getGlobalOptionId() != null) {
            GlobalOption globalOption = globalOptionRepository.findOptionAndValuesById(request.getGlobalOptionId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 옵션 템플릿이 존재하지 않습니다."));

            productOption.assignGlobalOption(globalOption);

            Set<GlobalOptionValue> globalOptionValues = globalOption.getGlobalOptionValues();
            request.getOptionValues().forEach(optionValueRequest -> {
                ProductOptionValue productOptionValue = generateProductOptionValue(optionValueRequest, globalOptionValues);
                productOption.addProductOptionValue(productOptionValue);
            });
        }
        else {
            request.getOptionValues().forEach(optionValueRequest -> {
                ProductOptionValue productOptionValue = generateProductOptionValue(optionValueRequest, Set.of());
                productOption.addProductOptionValue(productOptionValue);
            });
        }

        return productOption;
    }

    private ProductOptionValue generateProductOptionValue(ProductOptionValueRequest request, Set<GlobalOptionValue> globalOptionValues) {

        ProductOptionValue ret = ProductOptionValue.builder()
                .global(request.isGlobal())
                .optionValue(request.getOptionValue())
                .additionalPrice(request.getAdditionalPrice())
                .stockQuantity(request.getStockQuantity())
                .build();

        if (request.isGlobal() && request.getGlobalOptionValueId() != null) {
            for (GlobalOptionValue value : globalOptionValues) {
                if (value.getId() == request.getGlobalOptionValueId()) {
                    ret.assignGlobalOptionValue(value);
                    break;
                }
            }
        }

        return ret;
    }
}
