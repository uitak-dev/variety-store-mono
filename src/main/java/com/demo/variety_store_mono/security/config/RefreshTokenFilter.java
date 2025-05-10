package com.demo.variety_store_mono.security.config;

import com.demo.variety_store_mono.common.service.JwtAuthenticationService;
import com.demo.variety_store_mono.security.handler.CustomAuthenticationEntryPoint;
import com.demo.variety_store_mono.security.jwt.CustomBearerTokenResolver;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final CustomBearerTokenResolver bearerTokenResolver;
    private final JwtDecoder jwtDecoder;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final JwtProperties jwtProperties;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> EXCLUDE_URL_PATTERNS = List.of(
//            "/public/js/**", "/public/css/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return EXCLUDE_URL_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, servletPath));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String accessToken = bearerTokenResolver.resolve(request);
        if (accessToken != null) {
            try {
                // 정상 토큰이면 디코딩만 수행
                jwtDecoder.decode(accessToken);
            } catch (JwtException ex) {
                // 만료된 토큰이라면 Refresh Token 으로 갱신 시도
                String refreshToken = CookieUtil.getCookieValue(request, "refreshToken").orElse(null);
                if (refreshToken != null) {
                    try {
                        String newAccessToken = jwtAuthenticationService.refreshAccessToken(refreshToken);

                        // 응답 쿠키에 갱신된 토큰 추가
                        CookieUtil.addCookie(response, "accessToken", newAccessToken,
                                jwtProperties.getAccessTokenValidityMillis().intValue());

                        // 원래 요청 헤더를 덮어쓴 래퍼 생성
                        HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
                            @Override
                            public String getHeader(String name) {
                                if (HttpHeaders.AUTHORIZATION.equals(name)) {
                                    return "Bearer " + newAccessToken;
                                }
                                return super.getHeader(name);
                            }
                        };

                        System.out.println("== == == == == == ==");
                        System.out.println("update AccessToken using RefreshToken");
                        System.out.println("== == == == == == ==");

                        // 갱신된 토큰으로 재진입
                        chain.doFilter(wrapped, response);
                        return;
                    } catch (RuntimeException refreshEx) {

                        System.out.println("== == == == == == ==");
                        System.out.println("Error Processing RefreshToken");
                        System.out.println("== == == == == == ==");

                        // Refresh Token 도 유효하지 않으면 로그인 유도
                        SecurityContextHolder.clearContext();
                        entryPoint.commence(request, response, new InsufficientAuthenticationException("Refresh token expired"));
                        return;
                    }
                } else {

                    System.out.println("== == == == == == ==");
                    System.out.println("there is no RefreshToken");
                    System.out.println("== == == == == == ==");

                    // Refresh Token 이 없으면 로그인 유도
                    SecurityContextHolder.clearContext();
                    entryPoint.commence(request, response, new InsufficientAuthenticationException("Access token expired"));
                    return;
                }
            }
        }

        // 토큰 만료가 아니거나 퍼블릭 요청일 경우 정상 진행
        chain.doFilter(request, response);
    }
}
