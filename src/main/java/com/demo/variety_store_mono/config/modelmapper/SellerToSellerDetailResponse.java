package com.demo.variety_store_mono.config.modelmapper;

import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import com.demo.variety_store_mono.seller.entity.Seller;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class SellerToSellerDetailResponse implements Converter<Seller, SellerDetailResponse> {

    @Override
    public SellerDetailResponse convert(MappingContext<Seller, SellerDetailResponse> context) {

        Seller source = context.getSource();
        SellerDetailResponse dest = new SellerDetailResponse();
        if (source != null && source.getUser() != null) {

            // 사용자 기본 정보 매핑.
            dest.setId(source.getUser().getId());
            dest.setUserName(source.getUser().getUserName());
            dest.setEmail(source.getUser().getEmail());
            dest.setFirstName(source.getUser().getFirstName());
            dest.setLastName(source.getUser().getLastName());
            dest.setPhoneNumber(source.getUser().getPhoneNumber());

            // 필요한 경우, 권한 정보 매핑 추가.

        }
        // SellerDetailResponse 고유 필드
        dest.setCompanyName(source.getCompanyName());
        dest.setBusinessLicenseNumber(source.getBusinessLicenseNumber());
        return dest;
    }
}
