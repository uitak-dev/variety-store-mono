package com.demo.variety_store_mono.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtProperties {

    private String secretKey;

    // 액세스 토큰 유효시간 (밀리초 단위)
    private Long accessTokenValidityMillis;

    // 리프레시 토큰 유효시간 (밀리초 단위)
    private Long refreshTokenValidityMillis;
}
