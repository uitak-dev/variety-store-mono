package com.demo.variety_store_mono.security.config;

import com.demo.variety_store_mono.security.handler.CustomAccessDeniedHandler;
import com.demo.variety_store_mono.security.handler.CustomAuthenticationEntryPoint;
import com.demo.variety_store_mono.security.handler.JwtLogoutHandler;
import com.demo.variety_store_mono.security.jwt.CustomBearerTokenResolver;
import com.demo.variety_store_mono.security.jwt.CustomJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomBearerTokenResolver bearerTokenResolver;
    private final RefreshTokenFilter refreshTokenFilter;
    private final JwtLogoutHandler jwtLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // BearerTokenAuthenticationFilter 실행 전에 커스텀 필터 실행.
                .addFilterBefore(refreshTokenFilter, BearerTokenAuthenticationFilter.class)
                // JWT 기반 인증 활성화
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(bearerTokenResolver)
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter()))
                )
                // 인가 여부 수행(인증 여부에 대한 필터가 먼저 실행)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/auth/**", "/api/**", "/error/**", "/public/**").permitAll()
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")                   // 로그아웃 요청 URL
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler((req, res, auth) -> {  // 성공 시 동작
                            res.setStatus(HttpStatus.OK.value());
                            res.sendRedirect("/");           // index.html로 리다이렉트
                        })
                );

        return http.build();
    }
}
