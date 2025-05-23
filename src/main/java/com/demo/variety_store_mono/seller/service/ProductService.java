package com.demo.variety_store_mono.seller.service;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.admin.entity.GlobalOption;
import com.demo.variety_store_mono.admin.entity.GlobalOptionValue;
import com.demo.variety_store_mono.admin.repository.CategoryRepository;
import com.demo.variety_store_mono.admin.repository.GlobalOptionRepository;
import com.demo.variety_store_mono.common.entity.UploadFile;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.dto.request.UploadFileRequest;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import com.demo.variety_store_mono.seller.entity.*;
import com.demo.variety_store_mono.seller.repository.ProductRepository;
import com.demo.variety_store_mono.seller.dto.request.ProductOptionRequest;
import com.demo.variety_store_mono.seller.dto.request.ProductOptionValueRequest;
import com.demo.variety_store_mono.seller.dto.request.ProductRequest;
import com.demo.variety_store_mono.seller.dto.search.SearchProduct;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import com.demo.variety_store_mono.utility.FileStore;
import com.demo.variety_store_mono.utility.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GlobalOptionRepository globalOptionRepository;

    private final ModelMapper modelMapper;
    private final FileStore fileStore;

    public ProductService(ProductRepository productRepository, UserRepository userRepository,
                          CategoryRepository categoryRepository, GlobalOptionRepository globalOptionRepository,
                          ModelMapper modelMapper, @Qualifier("localFileStore") FileStore fileStore) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.globalOptionRepository = globalOptionRepository;
        this.modelMapper = modelMapper;
        this.fileStore = fileStore;
    }

    /** 상품 등록 */
    public ProductResponse createProduct(Long sellerId, ProductRequest request) {

        User user = userRepository.findUserDetailsById(sellerId).orElseThrow(() ->
                new EntityNotFoundException("등록된 판매자가 아닙니다."));

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException("등록되지 않은 카테고리 입니다."));

        Product product = Product.builder()
                .primaryCategory(category)
                .name(request.getName())
                .description(request.getDescription())
                .single(request.isSingle())
                .basePrice(request.getBasePrice())
                .stockQuantity(request.getStockQuantity())
                .manufactureDate(request.getManufactureDate())
                .attributes(request.getAttributes())
                .seller(user.getSeller())
                .build();

        // 상품 썸네일 등록.
        ProductImage thumbnail = ProductImage.builder()
                .uploadFile(new UploadFile(request.getThumbnail().getUploadFileName(),
                        request.getThumbnail().getStoreFileName())
                )
                .isThumbnail(true)
                .build();
        product.addImage(thumbnail);

        // 상품 이미지 등록.
        for (UploadFileRequest uploadFileRequest : request.getImages()) {
            UploadFile uploadFile = new UploadFile(uploadFileRequest.getUploadFileName(),
                    uploadFileRequest.getStoreFileName());

            ProductImage productImage = ProductImage.builder()
                    .uploadFile(uploadFile)
                    .isThumbnail(false)
                    .build();

            product.addImage(productImage);
        }

        // 옵션 상품인 경우, 옵션 등록.
        if (!request.isSingle() && request.getProductOptions() != null) {
            request.getProductOptions().forEach(optionRequest -> {
                ProductOption productOption = generateProductOption(optionRequest);
                product.addProductOption(productOption);
            });
        }

        Product savedProduct = productRepository.save(product);
        category.addProduct(product);

        return ProductMapper.toResponse(savedProduct);
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

        return ProductMapper.toResponse(product);
    }

    /** 상품 수정 */
    public ProductResponse updateProduct(Long productId, Long sellerId, ProductRequest request) {

        User user = userRepository.findUserDetailsById(sellerId).orElseThrow(() ->
                new EntityNotFoundException("등록된 판매자가 아닙니다."));

        // primaryCategory(판매자가 상품 등록 시 선택한 카테고리) 조회.
        Category primaryCategory = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new EntityNotFoundException("등록되지 않은 카테고리 입니다."));

        Product product = productRepository.findProductDetails(sellerId, productId).orElseThrow(() ->
                new EntityNotFoundException("등록되지 않은 상품 입니다."));

        // 상품 기본 정보 수정.
        product.updateProduct(
                primaryCategory, request.getName(), request.getDescription(), request.isSingle(),
                request.getBasePrice(), request.getManufactureDate(),
                request.getStockQuantity(), request.getAttributes()
        );

        // 상품 썸네일 수정.
        if (product.getThumbnail() == null) {
            ProductImage thumbnail = ProductImage.builder()
                    .uploadFile(new UploadFile(request.getThumbnail().getUploadFileName(),
                            request.getThumbnail().getStoreFileName())
                    )
                    .isThumbnail(true)
                    .build();
            product.setThumbnail(thumbnail);
        }

        // 상품 이미지 수정.
        for (UploadFileRequest uploadFileRequest : request.getImages()) {
            UploadFile uploadFile = new UploadFile(uploadFileRequest.getUploadFileName(),
                    uploadFileRequest.getStoreFileName());

            List<String> list = product.getImages().stream()
                    .map(productImage -> productImage.getUploadFile().getStoreFileName())
                    .toList();

            if (list.contains(uploadFile.getStoreFileName())) continue;

            ProductImage productImage = ProductImage.builder()
                    .uploadFile(uploadFile)
                    .isThumbnail(false)
                    .build();

            product.addImage(productImage);
        }

        // 상품 옵션 관련 처리
        if (request.isSingle()) {
            // 단일 상품으로 수정하는 경우: 기존 옵션이 있다면 모두 삭제.
            if (!product.getProductOptions().isEmpty()) {
                product.getProductOptions().clear();
            }
        } else {
            // 옵션 상품으로 수정하는 경우: 기존 옵션 정보를 초기화 후 새로 생성.
            product.getProductOptions().clear();  // orphanRemoval 옵션에 의해 기존 옵션 및 옵션 값이 삭제됨.
            if (request.getProductOptions() != null) {
                request.getProductOptions().forEach(optionRequest -> {
                    ProductOption productOption = generateProductOption(optionRequest);
                    product.addProductOption(productOption);
                });
            }
        }

        // 카테고리 연관 업데이트: 기존 등록된 카테고리 중, 기본 카테고리와 동일한 항목은 유지하고, 나머지만 제거
        Optional<ProductCategory> existingPrimaryAssociation = product.getProductCategories().stream()
                .filter(pc -> pc.getCategory().getId().equals(primaryCategory.getId()))
                .findFirst();

        if (existingPrimaryAssociation.isPresent()) {
            // 기본 카테고리와 다른 항목 삭제 (변경 감지에 의해 flush 시 삭제 처리됨)
            product.getProductCategories().removeIf(pc -> !pc.getCategory().getId().equals(primaryCategory.getId()));
        } else {
            // 기본 카테고리가 없으면 기존 연관 모두 제거 후 새로운 기본 카테고리 연결
            product.getProductCategories().clear();
            product.getProductCategories().add(new ProductCategory(product, primaryCategory));
        }

        // 수정된 상품 저장
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toResponse(savedProduct);
    }

    /** 상품 삭제 */


    /** 상품 이미지 삭제 */
    @Transactional
    public void deleteProductImage(Long productId, String storeFileName) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found. id=" + productId));

        ProductImage target = product.getProductImages().stream()
                .filter(images -> images.getUploadFile().getStoreFileName().equals(storeFileName))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "ProductImage not found. storeFileName=" + storeFileName));

        // 물리 파일 삭제

        // 논리 파일 삭제(DB에 저장된 데이터 제거)
        product.removeImage(target);
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
