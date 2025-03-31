package com.demo.variety_store_mono.security.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증은 되었지만, 접근 권한이 없는 경우 호출.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 요청의 Accept 헤더가 HTML을 포함하는 경우, 에러 페이지로 포워딩
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            forwardToErrorPage(request, response, accessDeniedException);
        }
        else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        }
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response,
                                    AccessDeniedException ex) throws ServletException, IOException {

        // BasicErrorController가 사용할 표준 오류 속성 설정
        request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_FORBIDDEN);
        request.setAttribute("javax.servlet.error.message", ex.getMessage());

        // 서블릿 컨테이너의 명명된 에러 디스패처(named dispatcher)를 이용해 forward 처리
        RequestDispatcher dispatcher = request.getServletContext().getNamedDispatcher("error");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
    }
}
