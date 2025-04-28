package com.demo.variety_store_mono.seller.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class ProfileController {

    /** 판매자 프로필 페이지 */
    @GetMapping("/profiles")
    public String profilePage() {
        return "seller/content/home";
    }


}
