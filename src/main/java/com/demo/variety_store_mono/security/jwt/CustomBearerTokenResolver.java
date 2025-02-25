package com.demo.variety_store_mono.security.jwt;

import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        // Authorization 헤더 확인
        String headerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            return headerToken.substring(7);
        }

        // 쿠키에서 accessToken 가져오기
        return CookieUtil.getCookieValue(request, "accessToken").orElse(null);
    }
}