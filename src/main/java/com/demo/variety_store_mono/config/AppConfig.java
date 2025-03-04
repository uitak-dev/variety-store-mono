package com.demo.variety_store_mono.config;

import com.demo.variety_store_mono.admin.response.AdminDetailResponse;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import com.demo.variety_store_mono.customer.response.CustomerDetailResponse;
import com.demo.variety_store_mono.seller.response.SellerDetailResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
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

}
