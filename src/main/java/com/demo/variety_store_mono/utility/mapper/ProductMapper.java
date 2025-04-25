package com.demo.variety_store_mono.utility.mapper;

import com.demo.variety_store_mono.admin.dto.summary.CategorySummary;
import com.demo.variety_store_mono.seller.dto.response.ProductOptionResponse;
import com.demo.variety_store_mono.seller.dto.response.ProductResponse;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductImage;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private static final ModelMapper basicMapper = new ModelMapper();

    public static ProductResponse toResponse(Product product) {

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
        CategorySummary primaryCategory = basicMapper.map(product.getPrimaryCategory(), CategorySummary.class);

        // 3) 썸네일 매핑
        UploadFileResponse thumbnail = basicMapper.map(product.getThumbnail().getUploadFile(), UploadFileResponse.class);

        // 4) 기타 이미지 매핑
        List<UploadFileResponse> images = product.getProductImages().stream()
                .filter(productImage -> !productImage.isThumbnail())
                .map(ProductImage::getUploadFile)
                .map(uploadFile -> basicMapper.map(uploadFile, UploadFileResponse.class))
                .toList();

        // 5) 옵션 매핑
        List<ProductOptionResponse> productOptions = product.getProductOptions().stream()
                .map(productOption -> basicMapper.map(productOption, ProductOptionResponse.class))
                .toList();

        return ProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .basePrice(basePrice)
                .manufactureDate(manufactureDate)
                .stockQuantity(stockQuantity)
                .single(single)
                .status(status)
                .primaryCategory(primaryCategory)
                .thumbnail(thumbnail)
                .images(images)
                .productOptions(productOptions)
                .build();
    }
}
