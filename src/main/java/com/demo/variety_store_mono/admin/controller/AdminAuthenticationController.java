package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.request.LoginRequest;
import com.demo.variety_store_mono.common.request.SignUpRequest;
import com.demo.variety_store_mono.common.response.TokenResponse;
import com.demo.variety_store_mono.common.service.AuthenticationService;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    private final JwtProperties jwtProperties;

    /** 관리자 로그인 페이지 */
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    /** 관리자 로그인 API */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpServletResponse response) {

        // Access/Refresh 토큰 생성 및 저장.
        TokenResponse tokenResponse = authenticationService.login(request);

        // Access Token 쿠키에 저장.
        CookieUtil.addCookie(response, "accessToken",
                tokenResponse.getAccessToken(), jwtProperties.getAccessTokenValidityMillis().intValue());

        // Refresh Token 의 UUID를 세션 쿠키에 저장.( 클라이언트에서 직접 접근 불가 )
        CookieUtil.addCookie(response, "refreshTokenId",
                tokenResponse.getRefreshTokenId().toString(), -1);

        return "redirect:/admin/home";
    }

    /** 관리자 회원가입 페이지 */
    @GetMapping("/signup")
    public String signupPage() {
        return "admin/signup";
    }

    /** 관리자 회원가입 API */
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequest request) {
        userService.createUser(request, UserType.ADMIN);
        return "redirect:/auth/admin/login";
    }

    /** 관리자 리프레시 토큰 API */
    @ResponseBody
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        // 세션에서 Refresh Token ID 확인
        String refreshTokenId = CookieUtil.getCookieValue(request, "refreshTokenId").orElse(null);
        if (refreshTokenId == null) {
            return ResponseEntity.status(401).body("Missing refresh token");
        }

        // 새로운 Access Token 발급.
        String newAccessToken = authenticationService.refreshAccessToken(Long.parseLong(refreshTokenId));
        // 발급받은 Access Token을 쿠키에 저장.
        CookieUtil.addCookie(response, "accessToken", newAccessToken, jwtProperties.getAccessTokenValidityMillis().intValue());

        return ResponseEntity.ok("New access token issued");
    }

    /** 관리자 로그아웃 API */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        // DB에서 Refresh Token 삭제
        String refreshTokenId = CookieUtil.getCookieValue(request, "refreshTokenId").orElse(null);
        if (refreshTokenId != null) {
            authenticationService.logout(Long.parseLong(refreshTokenId));
        }

        // 세션 및 쿠키에서 Access Token & Refresh Token 삭제
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshTokenId");

        return "redirect:/auth/admin/login";
    }
}
