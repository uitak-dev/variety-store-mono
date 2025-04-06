package com.demo.variety_store_mono.admin.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminHomeController {

    /** 관리자 홈 페이지 */
    @GetMapping("/home")
    public String homePage() {
        return "admin/content/home";
    }
}
