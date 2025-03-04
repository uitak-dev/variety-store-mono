package com.demo.variety_store_mono.config.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(new UserToAdminInfoResponse());
        mapper.addConverter(new UserToSellerInfoResponse());
        mapper.addConverter(new UserToCustomerInfoResponse());

        return mapper;
    }
}
