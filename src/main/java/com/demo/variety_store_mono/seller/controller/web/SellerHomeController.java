package com.demo.variety_store_mono.seller.controller.web;

import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategy;
import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategyFactory;
import com.demo.variety_store_mono.common.dto.form.AdminProfileForm;
import com.demo.variety_store_mono.common.dto.form.SellerProfileForm;
import com.demo.variety_store_mono.common.dto.response.TokenResponse;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.utility.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class SellerHomeController {

    private final UserDetailStrategyFactory strategyFactory;
    private final JwtProperties jwtProperties;

    /** 판매자 대시보드 페이지 */
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "seller/content/dashboard";
    }

    /** 사용자 프로필 페이지 */
    @GetMapping("/profiles")
    public String profilePage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 판매자 계정이 아닌 경우, 판매자 도메인의 프로필에 접근 불가.
        if (UserType.SELLER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("profile", strategy.getProfile(userId));

        return "seller/content/profile/profile-detail";
    }

    /** 사용자 프로필 수정 페이지 */
    @GetMapping("/profiles/edit")
    public String profileEditPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 관리자 계정이 아닌 경우, 관리자 도메인의 프로필에 접근 불가.
        if (UserType.SELLER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("profile", strategy.getProfile(userId));

        return "seller/content/profile/profile-edit";
    }

    /** 사용자 프로필 수정 API */
    @PostMapping("/profiles/edit")
    public String editProfile(@AuthenticationPrincipal Jwt jwt, @ModelAttribute("profile") @Valid SellerProfileForm request,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "seller/content/profile/profile-edit";
        }

        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 판매자 계정이 아닌 경우, 판매자 도메인의 프로필에 접근 불가.
        if (UserType.SELLER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        strategy.updateProfile(userId, request);

        return "redirect:/seller/profiles";
    }

    /** 사용자 아이디 수정 페이지 */
    @GetMapping("/userName/edit")
    public String userNameEditPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        String userName = jwt.getSubject();
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 관리자 계정이 아닌 경우, 관리자 도메인의 프로필에 접근 불가.
        if (UserType.SELLER != userType) {
            return "error/403";
        }

        model.addAttribute("userName", userName);
        return "seller/content/profile/username-edit";
    }

    /** 사용자 아이디 수정 API */
    @PostMapping("/userName/edit")
    public String editUserName(@AuthenticationPrincipal Jwt jwt, Model model, HttpServletResponse response,
                               @RequestParam String newUserName, @RequestParam String password) {

        Long userId = jwt.getClaim("id");
        String userName = jwt.getSubject();
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 관리자 계정이 아닌 경우, 관리자 도메인의 프로필에 접근 불가.
        if (UserType.SELLER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        try {
            TokenResponse tokenResponse = strategy.updateUserName(userId, newUserName, password);

            // Access Token 세션 쿠키에 저장.
            CookieUtil.addCookie(response, "accessToken",
                    tokenResponse.getAccessToken(), -1);

            // Refresh Token 을 영속 쿠키에 저장.( 클라이언트에서 직접 접근 불가 )
            CookieUtil.addCookie(response, "refreshToken",
                    tokenResponse.getRefreshToken(), jwtProperties.getRefreshTokenValidityMillis().intValue());

        } catch (RuntimeException ex) {
            model.addAttribute("userName", userName);
            model.addAttribute("error", ex.getMessage());
            return "seller/content/profile/username-edit";
        }

        return "redirect:/seller/profiles";
    }
}
