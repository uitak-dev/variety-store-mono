package com.demo.variety_store_mono.security.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 경우 호출.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        String loginUrl;

        // 요청 URL에 따라 적절한 로그인 페이지 지정.
        if (requestUri.startsWith("/admin")) {
            loginUrl = "/auth/admin/login";
        } else if (requestUri.startsWith("/seller")) {
            loginUrl = "/auth/seller/login";
        } else {
            loginUrl = "/auth/user/login";
        }

        // 요청의 Accept 헤더가 HTML을 포함하는 경우, 에러 페이지로 포워딩
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            forwardToErrorPage(request, response, authException, loginUrl);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response,
                                    AuthenticationException ex, String loginUrl) throws ServletException, IOException {

        // BasicErrorController가 사용할 표준 오류 속성 설정
        request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_UNAUTHORIZED);
        request.setAttribute("javax.servlet.error.message", ex.getMessage());

        // 필요에 따라 추가 커스텀 데이터 설정.
        request.setAttribute("loginUrl", loginUrl);

        // 서블릿 컨테이너의 명명된 에러 디스패처(named dispatcher)를 이용해 forward 처리
        RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("error");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

}
