package com.demo.variety_store_mono.seller.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerHomeController {

    /** 판매자 홈 페이지 */
    @GetMapping("/home")
    public String homePage() {
        return "seller/content/home";
    }
}
