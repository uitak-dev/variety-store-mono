package com.demo.variety_store_mono.config.modelmapper;

import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import com.demo.variety_store_mono.customer.response.CustomerDetailResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class UserToCustomerInfoResponse implements Converter<User, CustomerDetailResponse> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CustomerDetailResponse convert(MappingContext<User, CustomerDetailResponse> context) {
        User source = context.getSource();
        if (source == null) {
            return null;
        }

        // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
        CustomerDetailResponse destination = modelMapper.map(source, CustomerDetailResponse.class);

        // 일반 사용자 상세 정보 설정.
        if (source.getCustomerDetail() != null) {
            destination.setAddress(source.getCustomerDetail().getAddress());
        }

        return destination;
    }
}
