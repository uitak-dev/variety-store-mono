package com.demo.variety_store_mono.admin.converter;

import com.demo.variety_store_mono.security.entity.UserType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserTypeConverter implements Converter<String, UserType> {

    @Override
    public UserType convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return UserType.CUSTOMER; // 기본값
        }
        try {
            return UserType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 변환 실패 시 기본값 또는 예외 처리
            return UserType.CUSTOMER;
        }
    }
}
