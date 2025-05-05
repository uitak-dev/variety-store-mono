package com.demo.variety_store_mono.customer.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CustomerHomeController {

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
}
