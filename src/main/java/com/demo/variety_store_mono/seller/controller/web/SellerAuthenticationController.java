package com.demo.variety_store_mono.seller.controller.web;

import com.demo.variety_store_mono.common.request.LoginRequest;
import com.demo.variety_store_mono.common.request.SignUpRequest;
import com.demo.variety_store_mono.common.response.TokenResponse;
import com.demo.variety_store_mono.common.service.JwtAuthenticationService;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/seller")
@RequiredArgsConstructor
public class SellerAuthenticationController {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserService userService;

    private final JwtProperties jwtProperties;

    /** 판매자 로그인 페이지 */
    @GetMapping("/login")
    public String loginPage() {
        return "seller/login";
    }

    /** 판매자 로그인 API */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {

        try {
            // Access/Refresh 토큰 생성 및 저장.
            TokenResponse tokenResponse = jwtAuthenticationService.login(request);

            // Access Token 세션 쿠키에 저장.
            CookieUtil.addCookie(response, "accessToken",
                    tokenResponse.getAccessToken(), -1);

            // Refresh Token 을 영속 쿠키에 저장.( 클라이언트에서 직접 접근 불가 )
            CookieUtil.addCookie(response, "refreshToken",
                    tokenResponse.getRefreshToken(), jwtProperties.getRefreshTokenValidityMillis().intValue());

            return "redirect:/seller/dashboard";
        }
        catch(RuntimeException ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
            return "redirect:/auth/seller/login";
        }
    }

    /** 판매자 회원가입 페이지 */
    @GetMapping("/signup")
    public String signupPage() {
        return "seller/signup";
    }

    /** 판매자 회원가입 API */
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequest request) {
        userService.createUser(request, UserType.SELLER);
        return "redirect:/auth/seller/login";
    }

    /** 판매자 리프레시 토큰 API */
    @ResponseBody
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        // 세션에서 Refresh Token ID 확인
        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken").orElse(null);
        if (refreshToken == null) {
            return ResponseEntity.status(401).body("Missing refresh token");
        }

        // 새로운 Access Token 발급.
        String newAccessToken = jwtAuthenticationService.refreshAccessToken(refreshToken);

        // 발급받은 Access Token을 쿠키에 저장.
        CookieUtil.addCookie(response, "accessToken",
                newAccessToken, -1);

        return ResponseEntity.ok("New access token issued");
    }

    /** 판매자 로그아웃 API */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        // DB에서 Refresh Token 삭제
        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken").orElse(null);
        if (refreshToken != null) {
            jwtAuthenticationService.logout(refreshToken);
        }

        // 세션 및 쿠키에서 Access Token & Refresh Token 삭제
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");

        return "redirect:/auth/seller/login";
    }
}
