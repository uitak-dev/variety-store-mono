package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.admin.request.AdminInfoRequest;
import com.demo.variety_store_mono.admin.response.AdminInfoResponse;
import com.demo.variety_store_mono.admin.service.AdminService;
import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.admin.request.UserSearch;
import com.demo.variety_store_mono.common.response.UserInfoResponse;
import com.demo.variety_store_mono.customer.request.CustomerInfoRequest;
import com.demo.variety_store_mono.customer.response.CustomerInfoResponse;
import com.demo.variety_store_mono.seller.request.SellerInfoRequest;
import com.demo.variety_store_mono.seller.response.SellerInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final AdminService adminService;

    /** 사용자 목록 조회 페이지 */
    @GetMapping
    public String userList(
            @RequestParam(defaultValue = "customer") UserType userType,
            @ModelAttribute UserSearch userSearch, Pageable pageable, Model model) {

        // 메뉴 목록 생성 (예: 일반 사용자, 판매자, 관리자)
        List<SidebarMenu> menuItems = new ArrayList<>();
        menuItems.add(new SidebarMenu("/admin/users?userType=admin", "관리자", "admin".equalsIgnoreCase(userType.name())));
        menuItems.add(new SidebarMenu("/admin/users?userType=seller", "판매자", "seller".equalsIgnoreCase(userType.name())));
        menuItems.add(new SidebarMenu("/admin/users?userType=customer", "일반 사용자", "customer".equalsIgnoreCase(userType.name())));
        model.addAttribute("menuItems", menuItems);

        // 페이지 네비게이션 링크 등에 사용하기 위해, userType 도 모델에 담습니다.
        model.addAttribute("userType", userType.name());

        Page<UserInfoResponse> userList = adminService.getUserList(userType, userSearch, pageable);
        model.addAttribute("userList", userList);

        return "admin/content/user-list";
    }

    /** 사용자 상세 페이지 */
    @GetMapping("/{userId}")
    public String userDetails(@PathVariable Long userId,
                              @RequestParam UserType userType, Model model) {

        model.addAttribute("userType", userType.name());

        if (userType.name().equals("ADMIN")) {
            AdminInfoResponse adminInfo = adminService.getAdminInfo(userId);
            model.addAttribute("userDetailInfo", adminInfo);
            return "admin/content/detail/admin-detail";
        }
        else if (userType.name().equals("SELLER")) {
            SellerInfoResponse sellerInfo = adminService.getSellerInfo(userId);
            model.addAttribute("userDetailInfo", sellerInfo);
            return "admin/content/detail/seller-detail";
        }
        else if (userType.name().equals("CUSTOMER")) {
            CustomerInfoResponse customerInfo = adminService.getCustomerInfo(userId);
            model.addAttribute("userDetailInfo", customerInfo);
            return "admin/content/detail/customer-detail";
        }

        throw new RuntimeException("잘못된 사용자 유형.");
    }

    /** 사용자 수정 페이지 */
    @GetMapping("/{userId}/edit")
    public String userEditPage(@PathVariable Long userId,
                               @RequestParam UserType userType, Model model) {

        model.addAttribute("userType", userType.name());

        if (userType.name().equals("ADMIN")) {
            AdminInfoResponse adminInfo = adminService.getAdminInfo(userId);
            model.addAttribute("userDetailInfo", adminInfo);
            return "admin/content/edit/admin-edit";
        }
        else if (userType.name().equals("SELLER")) {
            SellerInfoResponse sellerInfo = adminService.getSellerInfo(userId);
            model.addAttribute("userDetailInfo", sellerInfo);
            return "admin/content/edit/seller-edit";
        }
        else if (userType.name().equals("CUSTOMER")) {
            CustomerInfoResponse customerInfo = adminService.getCustomerInfo(userId);
            model.addAttribute("userDetailInfo", customerInfo);
            return "admin/content/edit/customer-edit";
        }

        throw new RuntimeException("잘못된 사용자 유형.");
    }

    /** 관리자 정보 수정 API */
    @PostMapping(value = "/{userId}/edit", params = "userType=ADMIN")
    public String editAdminInfo(@PathVariable Long userId, @RequestParam UserType userType,
                               AdminInfoRequest request, RedirectAttributes redirectAttributes) {

        adminService.updateAdminInfo(userId, request);

        redirectAttributes.addAttribute("userType", userType.name());
        return "redirect:/admin/users/{userId}";
    }

    /** 판매자 정보 수정 API */
    @PostMapping(value = "/{userId}/edit", params = "userType=SELLER")
    public String editSellerInfo(@PathVariable Long userId, @RequestParam UserType userType,
                                 SellerInfoRequest request, RedirectAttributes redirectAttributes) {

        adminService.updateSellerInfo(userId, request);

        redirectAttributes.addAttribute("userType", userType.name());
        return "redirect:/admin/users/{userId}";
    }

    /** 고객 정보 수정 API */
    @PostMapping(value = "/{userId}/edit", params = "userType=CUSTOMER")
    public String editCustomerInfo(@PathVariable Long userId, @RequestParam UserType userType,
                                 CustomerInfoRequest request, RedirectAttributes redirectAttributes) {

        adminService.updateCustomerInfo(userId, request);

        redirectAttributes.addAttribute("userType", userType.name());
        return "redirect:/admin/users/{userId}";
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SidebarMenu {
        private String url;
        private String label;
        private boolean active;
    }
}
