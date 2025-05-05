package com.demo.variety_store_mono.utility.mapper;

import com.demo.variety_store_mono.customer.dto.summary.ProductCatalogSummary;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.dto.response.UploadFileResponse;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class ProductCatalogSummaryMapper {

    private static final ModelMapper basicMapper = new ModelMapper();

    public static ProductCatalogSummary toResponse(Product product) {

        // 1) 기본 필드
        Long id = product.getId();
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal basePrice = product.getBasePrice();

        // 2) 썸네일 이미지 매핑
        UploadFileResponse thumbnail = basicMapper.map(product.getThumbnail().getUploadFile(), UploadFileResponse.class);

        // 3 ) 판매자 정보 매핑
        Seller seller = product.getSeller();
        User user = seller.getUser();
        SellerDetailResponse sellerDetailResponse = new SellerDetailResponse(seller.getCompanyName(), seller.getBusinessLicenseNumber());
        sellerDetailResponse.setId(user.getId());
        sellerDetailResponse.setUserName(user.getUserName());
        sellerDetailResponse.setEmail(user.getEmail());
        sellerDetailResponse.setFirstName(user.getFirstName());
        sellerDetailResponse.setLastName(user.getLastName());
        sellerDetailResponse.setPhoneNumber(user.getPhoneNumber());

        return ProductCatalogSummary.builder()
                .id(id)
                .name(name)
                .thumbnail(thumbnail)
                .description(description)
                .basePrice(basePrice)
                .seller(sellerDetailResponse)
                .build();
    }
}
