package com.demo.variety_store_mono.security.handler;

import com.demo.variety_store_mono.common.service.JwtAuthenticationService;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtAuthenticationService jwtAuthenticationService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1) 쿠키에서 refreshToken 조회
        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken").orElse(null);
        // 2) 저장소에서 Refresh Token 삭제
        if (refreshToken != null) {
            jwtAuthenticationService.logout(refreshToken);
        }
        // 3) 클라이언트 쿠키 삭제
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");
        // 4) SecurityContext 클리어
        SecurityContextHolder.clearContext();
    }
}
