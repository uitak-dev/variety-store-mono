package com.demo.variety_store_mono.config.modelmapper;

import com.demo.variety_store_mono.admin.response.AdminDetailResponse;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class UserToAdminInfoResponse implements Converter<User, AdminDetailResponse> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public AdminDetailResponse convert(MappingContext<User, AdminDetailResponse> context) {
        User source = context.getSource();
        if (source == null) {
            return null;
        }

        // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
        AdminDetailResponse destination = modelMapper.map(source, AdminDetailResponse.class);


        // 관리자 상세 정보 설정.
        if (source.getAdminDetail() != null) {
            destination.setDepartment(source.getAdminDetail().getDepartment());
        }

        return destination;
    }
}