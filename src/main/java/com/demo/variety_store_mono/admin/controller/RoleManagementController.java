package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.admin.request.RoleRequest;
import com.demo.variety_store_mono.admin.request.RoleSearch;
import com.demo.variety_store_mono.admin.request.UserSearch;
import com.demo.variety_store_mono.admin.response.RoleResponse;
import com.demo.variety_store_mono.admin.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final RoleService roleService;

    /** 역할 목록 조회 페이지 */
    @GetMapping
    public String roleList(@ModelAttribute RoleSearch roleSearch,
                           Pageable pageable, Model model) {

        Page<RoleResponse> roleList = roleService.getRoleList(roleSearch, pageable);
        model.addAttribute("roleList", roleList);

        return "admin/content/roles/role-list";
    }

    /** 역할 상세 조회 페이지 */
    @GetMapping("/{roleId}")
    public String roleDetails(@PathVariable Long roleId, Model model) {

        RoleResponse role = roleService.getRole(roleId);
        model.addAttribute("role", role);

        return "admin/content/roles/role-detail";
    }

    /** 역할 등록 페이지 */
    @GetMapping("/{roleId}/new")
    public String roleNew(@PathVariable Long roleId) {
        return null;
    }

    /** 역할 수정 페이지 */
    @GetMapping("/{roleId}/edit")
    public String roleUpdate(@PathVariable Long roleId, Model model) {

        RoleResponse role = roleService.getRole(roleId);
        model.addAttribute("role", role);

        return "admin/content/roles/role-edit";
    }

    /** 역할 수정 API */
    @PostMapping("/{roleId}/edit")
    public String updateRole(@PathVariable Long roleId, RoleRequest request, Model model) {

        roleService.update(roleId, request);
        return "redirect:/admin/roles/{roleId}";
    }
}
