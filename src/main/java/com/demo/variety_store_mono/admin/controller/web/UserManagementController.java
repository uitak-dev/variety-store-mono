package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.admin.dto.search.SearchUser;
import com.demo.variety_store_mono.admin.resolver.UserDetail;
import com.demo.variety_store_mono.admin.service.RoleService;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategy;
import com.demo.variety_store_mono.admin.service.strategy.UserDetailStrategyFactory;
import com.demo.variety_store_mono.common.service.UserService;
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

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailStrategyFactory strategyFactory;

    /** 사용자 목록 조회 페이지 */
    @GetMapping
    public String userList(
            @RequestParam(defaultValue = "customer") UserType userType,
            @ModelAttribute SearchUser searchUser, Pageable pageable, Model model) {

        // 페이지 네비게이션 링크 등에 사용하기 위해, userType 도 모델에 담습니다.
        model.addAttribute("userType", userType.name().toLowerCase());

        Page<UserBasicInfoResponse> userList = userService.getUserList(userType, searchUser, pageable);
        model.addAttribute("userList", userList);

        return "admin/content/users/user-list";
    }

    /** 사용자 상세 페이지 */
    @GetMapping("/{userId}")
    public String userDetails(@PathVariable Long userId,
                              @RequestParam UserType userType, Model model) {

        model.addAttribute("userType", userType.name().toLowerCase());
        model.addAttribute("allRoles", roleService.getRoles());

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("userDetailInfo", strategy.getDetail(userId));

        return strategy.getDetailView();
    }

    /** 사용자 수정 페이지 */
    @GetMapping("/{userId}/edit")
    public String userEditPage(@PathVariable Long userId,
                               @RequestParam UserType userType, Model model) {

        model.addAttribute("userType", userType.name().toLowerCase());
        model.addAttribute("allRoles", roleService.getRoles());

        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        model.addAttribute("userDetailInfo", strategy.getDetail(userId));

        return strategy.getEditView();
    }

    /** 사용자 수정 API */
    @PostMapping("/{userId}/edit")
    public String editUserDetail(@PathVariable Long userId,
                                 @RequestParam UserType userType,
                                 @UserDetail UserBasicInfoRequest request,
                                 RedirectAttributes redirectAttributes) {

        // userDetailRequest는 커스텀 ArgumentResolver 에 의해 구체 DTO로 바인딩됨.
        UserDetailStrategy strategy = strategyFactory.getStrategy(userType);
        strategy.updateDetail(userId, request);
        redirectAttributes.addAttribute("userType", userType.name().toLowerCase());
        return "redirect:/admin/users/{userId}";
    }
}
