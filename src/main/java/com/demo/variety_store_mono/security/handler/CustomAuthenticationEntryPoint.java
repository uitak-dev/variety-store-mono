package com.demo.variety_store_mono.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 인증되지 않은 경우 호출.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String loginPage;

        // 요청 URL에 따라 적절한 로그인 페이지 지정.
        if (requestURI.startsWith(contextPath + "/auth/user")) {
            loginPage = contextPath + "auth/user/login";
        } else if (requestURI.startsWith(contextPath + "/auth/seller")) {
            loginPage = contextPath + "auth/seller/login";
        } else if (requestURI.startsWith(contextPath + "/auth/admin")) {
            loginPage = contextPath + "auth/admin/login";
        } else {
            // 예상되지 않는 도메인에 대한 요청인 경우 404 에러 반환.
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 도메인에 대한 로그인 페이지가 존재하지 않습니다.");
            return;
        }

        response.sendRedirect(loginPage);
    }
}
