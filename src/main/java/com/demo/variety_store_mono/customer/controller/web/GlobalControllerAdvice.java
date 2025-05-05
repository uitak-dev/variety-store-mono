package com.demo.variety_store_mono.customer.controller.web;

import com.demo.variety_store_mono.security.entity.UserType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(basePackages = "com.demo.variety_store_mono.customer.controller.web")
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAuthenticationAttributes(@AuthenticationPrincipal Jwt jwt, Model model) {
        boolean isAuthenticated = false;
        Long userId = (jwt != null) ? jwt.getClaim("id") : null;

        if (userId != null && jwt.getClaim("roles") != null) {
            List<String> roles = jwt.getClaim("roles");
            if (roles.contains(UserType.CUSTOMER.getRoleName())) {
                isAuthenticated = true;
                model.addAttribute("userId", userId);
                model.addAttribute("userName", jwt.getSubject());
            }
        }

        model.addAttribute("isAuthenticated", isAuthenticated);
    }
}
