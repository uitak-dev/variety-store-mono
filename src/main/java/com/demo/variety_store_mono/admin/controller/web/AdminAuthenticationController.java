package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.common.dto.request.LoginRequest;
import com.demo.variety_store_mono.common.dto.request.SignUpRequest;
import com.demo.variety_store_mono.common.dto.response.TokenResponse;
import com.demo.variety_store_mono.common.service.JwtAuthenticationService;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminAuthenticationController {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserService userService;

    private final JwtProperties jwtProperties;

    /** 관리자 로그인 페이지 */
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    /** 관리자 로그인 API */
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

            return "redirect:/admin/home";
        }
        catch(RuntimeException ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
            return "redirect:/auth/admin/login";
        }
    }

    /** 관리자 회원가입 페이지 */
    @GetMapping("/signup")
    public String signupPage() {
        return "admin/signup";
    }

    /** 관리자 회원가입 API */
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpRequest request, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(request, UserType.ADMIN);
        } catch (RuntimeException ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
            return "redirect:/auth/admin/signup";
        }
        return "redirect:/auth/admin/login";
    }
}
