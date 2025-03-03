package com.demo.variety_store_mono.config;

import com.demo.variety_store_mono.admin.response.AdminInfoResponse;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.response.UserInfoResponse;
import com.demo.variety_store_mono.customer.response.CustomerInfoResponse;
import com.demo.variety_store_mono.seller.response.SellerInfoResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(new UserToAdminInfoResponse());
        mapper.addConverter(new UserToSellerInfoResponse());
        mapper.addConverter(new UserToCustomerInfoResponse());

        return mapper;
    }

    private static class UserToAdminInfoResponse implements Converter<User, AdminInfoResponse> {

        private final ModelMapper modelMapper = new ModelMapper();
        @Override
        public AdminInfoResponse convert(MappingContext<User, AdminInfoResponse> context) {
            User source = context.getSource();
            if (source == null) {
                return null;
            }
            AdminInfoResponse destination = new AdminInfoResponse();

            // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
            UserInfoResponse basicUserInfo = modelMapper.map(source, UserInfoResponse.class);
            destination.setUserBasicInfo(basicUserInfo);

            if (source.getAdminDetail() == null) return destination;

            // 관리자 상세 정보 설정.
            destination.setDepartment(source.getAdminDetail().getDepartment());

            return destination;
        }
    }

    private static class UserToSellerInfoResponse implements Converter<User, SellerInfoResponse> {

        private final ModelMapper modelMapper = new ModelMapper();
        @Override
        public SellerInfoResponse convert(MappingContext<User, SellerInfoResponse> context) {
            User source = context.getSource();
            if (source == null) {
                return null;
            }
            SellerInfoResponse destination = new SellerInfoResponse();

            // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
            UserInfoResponse basicUserInfo = modelMapper.map(source, UserInfoResponse.class);
            destination.setUserBasicInfo(basicUserInfo);

            if (source.getSellerDetail() == null) return destination;

            // 판매자 상세 정보 설정.
            destination.setCompanyName(source.getSellerDetail().getCompanyName());
            destination.setBusinessLicenseNumber(source.getSellerDetail().getBusinessLicenseNumber());

            return destination;
        }
    }

    private static class UserToCustomerInfoResponse implements Converter<User, CustomerInfoResponse> {

        private final ModelMapper modelMapper = new ModelMapper();
        @Override
        public CustomerInfoResponse convert(MappingContext<User, CustomerInfoResponse> context) {
            User source = context.getSource();
            if (source == null) {
                return null;
            }
            CustomerInfoResponse destination = new CustomerInfoResponse();

            // 기본 사용자 정보 자동 매핑 (필드명이 동일하면 기본 매핑이 작동)
            UserInfoResponse basicUserInfo = modelMapper.map(source, UserInfoResponse.class);
            destination.setUserBasicInfo(basicUserInfo);

            if (source.getCustomerDetail() == null) return destination;

            // 일반 사용자 상세 정보 설정.
            destination.setAddress(source.getCustomerDetail().getAddress());

            return destination;
        }
    }
}
