package com.demo.variety_store_mono.customer.controller.web;

import com.demo.variety_store_mono.customer.dto.response.CartResponse;
import com.demo.variety_store_mono.customer.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 페이지(장바구니에 담긴 상품 목록)
    @GetMapping
    public String cartPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        Long userId = jwt.getClaim("id");
        CartResponse cartDetails = cartService.getCartDetails(userId);

        model.addAttribute("cart", cartDetails);

        return "/customer/content/cart/cart-detail";
    }

}
