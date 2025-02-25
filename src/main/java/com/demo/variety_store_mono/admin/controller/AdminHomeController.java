package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.request.UserSearch;
import com.demo.variety_store_mono.common.response.SimpleUserInfo;
import com.demo.variety_store_mono.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminHomeController {

    private final UserService userService;

    /** 관리자 홈 페이지 */
    @GetMapping("/home")
    public String homePage() {
        return "admin/home";
    }

    @GetMapping("/test")
    @ResponseBody
    public Page<SimpleUserInfo> getUserListTest(@RequestParam UserType userType,
                                                @RequestBody UserSearch userSearch) {
        return userService.getUserList(userType, userSearch, Pageable.ofSize(10));
    }
}
