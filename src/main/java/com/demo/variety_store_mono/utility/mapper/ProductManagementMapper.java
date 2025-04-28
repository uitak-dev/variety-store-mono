package com.demo.variety_store_mono.utility.mapper;

import com.demo.variety_store_mono.admin.dto.response.ProductManagementResponse;
import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.seller.dto.response.ProductOptionResponse;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import com.demo.variety_store_mono.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ProductManagementMapper {

    private static final ModelMapper basicMapper = new ModelMapper();

    public static ProductManagementResponse toResponse(Product product) {

        // 1) 기본 필드
        Long id = product.getId();
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal basePrice = product.getBasePrice();
        LocalDate manufactureDate = product.getManufactureDate();
        int stockQuantity = product.getStockQuantity();
        boolean single = product.isSingle();
        ProductStatus status = product.getStatus();

        // 2) 카테고리 매핑
        List<CategorySummary> primaryCategories = product.getCategories().stream()
                .map(category -> basicMapper.map(category, CategorySummary.class))
                .toList();

        // 3) 썸네일 매핑
        UploadFileResponse thumbnail = basicMapper.map(product.getThumbnail().getUploadFile(), UploadFileResponse.class);

        // 4) 기타 이미지 매핑
        List<UploadFileResponse> images = product.getProductImages().stream()
                .filter(productImage -> !productImage.isThumbnail())
                .map(productImage -> basicMapper.map(productImage.getUploadFile(), UploadFileResponse.class))
                .toList();

        // 5) 옵션 매핑
        List<ProductOptionResponse> productOptions = product.getProductOptions().stream()
                .map(productOption -> basicMapper.map(productOption, ProductOptionResponse.class))
                .toList();

        // 6 ) 판매자 정보 매핑
        Seller seller = product.getSeller();
        User user = seller.getUser();
        SellerDetailResponse sellerDetailResponse = new SellerDetailResponse(seller.getCompanyName(), seller.getBusinessLicenseNumber());
        sellerDetailResponse.setId(user.getId());
        sellerDetailResponse.setUserName(user.getUserName());
        sellerDetailResponse.setEmail(user.getEmail());
        sellerDetailResponse.setFirstName(user.getFirstName());
        sellerDetailResponse.setLastName(user.getLastName());
        sellerDetailResponse.setPhoneNumber(user.getPhoneNumber());

        return ProductManagementResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .basePrice(basePrice)
                .manufactureDate(manufactureDate)
                .stockQuantity(stockQuantity)
                .single(single)
                .status(status)
                .categories(primaryCategories)
                .thumbnail(thumbnail)
                .images(images)
                .productOptions(productOptions)
                .seller(sellerDetailResponse)
                .build();
    }
}
