package com.demo.variety_store_mono.config.modelmapper;

import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class UserToSellerInfoResponse implements Converter<User, SellerDetailResponse> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public SellerDetailResponse convert(MappingContext<User, SellerDetailResponse> context) {
        User source = context.getSource();
        if (source == null) {
            return null;
        }

        // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
        SellerDetailResponse destination = modelMapper.map(source, SellerDetailResponse.class);

        if (source.getSeller() != null) {
            destination.setCompanyName(source.getSeller().getCompanyName());
            destination.setBusinessLicenseNumber(source.getSeller().getBusinessLicenseNumber());
        }

        return destination;
    }
}
