package com.demo.variety_store_mono.security.jwt;

import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomBearerTokenResolver implements BearerTokenResolver {

    private final JwtTokenProvider jwtTokenProvider;

    // 공개 엔드포인트 목록.
    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            PathRequest.toStaticResources().atCommonLocations(),
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/public/js/**"),
            new AntPathRequestMatcher("/public/css/**"),
            new AntPathRequestMatcher("/public/images/**"),
            new AntPathRequestMatcher("/error/**")
    );

    @Override
    public String resolve(HttpServletRequest request) {

        // 요청이 공개 엔드포인트에 해당하면 토큰 추출을 건너뜁니다.
        if (PUBLIC_URLS.matches(request)) {
            return null;
        }

        // Authorization 헤더 확인
        String headerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            return headerToken.substring(7);
        }

        // 쿠키에서 accessToken 가져오기
        return CookieUtil.getCookieValue(request, "accessToken").orElse(null);
    }
}