package com.demo.variety_store_mono.customer.controller.web;

import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategy;
import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategyFactory;
import com.demo.variety_store_mono.common.dto.form.CustomerProfileForm;
import com.demo.variety_store_mono.common.dto.form.SellerProfileForm;
import com.demo.variety_store_mono.security.entity.UserType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CustomerHomeController {

    private final UserDetailStrategyFactory strategyFactory;

    // 인덱스 페이지
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    // 소비자 홈 페이지
    @GetMapping("/public/home")
    public String homePage() {
        return "customer/home";
    }


    /** 사용자 프로필 페이지 */
    @GetMapping("/customer/profiles")
    public String profilePage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 소비자 계정이 아닌 경우, 소비자 도메인의 프로필에 접근 불가.
        if (UserType.CUSTOMER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("profile", strategy.getProfile(userId));

        return "customer/content/profile/profile-detail";
    }

    /** 사용자 프로필 수정 페이지 */
    @GetMapping("/customer/profiles/edit")
    public String profileEditPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 소비자 계정이 아닌 경우, 소비자 도메인의 프로필에 접근 불가.
        if (UserType.CUSTOMER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("profile", strategy.getProfile(userId));

        return "customer/content/profile/profile-edit";
    }

    /** 사용자 프로필 수정 API */
    @PostMapping("/customer/profiles/edit")
    public String editProfile(@AuthenticationPrincipal Jwt jwt, @ModelAttribute("profile") @Valid CustomerProfileForm request,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "customer/content/profile/profile-edit";
        }

        Long userId = jwt.getClaim("id");
        UserType userType = UserType.valueOf(jwt.getClaim("userType"));

        // 소비자 계정이 아닌 경우, 소비자 도메인의 프로필에 접근 불가.
        if (UserType.CUSTOMER != userType) {
            return "error/403";
        }

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        try {
            strategy.updateProfile(userId, request);
        } catch (IllegalArgumentException ex) {
            bindingResult.addError(new FieldError("profile", "userName", "중복된 아이디 입니다."));
            return "customer/content/profile/profile-edit";
        }

        return "redirect:/customer/profiles";
    }
}
