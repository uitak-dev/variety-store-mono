package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.admin.dto.request.RoleRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchRole;
import com.demo.variety_store_mono.admin.dto.response.RoleResponse;
import com.demo.variety_store_mono.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final RoleService roleService;

    /** 역할 목록 조회 페이지 */
    @GetMapping
    public String roleList(@ModelAttribute SearchRole searchRole,
                           Pageable pageable, Model model) {

        Page<RoleResponse> roleList = roleService.getRoleSearchList(searchRole, pageable);
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
    @GetMapping("/new")
    public String roleNew(Model model) {
        model.addAttribute("role", new RoleRequest());
        return "admin/content/roles/role-new";
    }

    /** 역할 등록 API */
    @PostMapping("/new")
    public String newRole(RoleRequest request, RedirectAttributes redirectAttributes) {
        RoleResponse role = roleService.createRole(request);
        redirectAttributes.addAttribute("roleId", role.getId());
        return "redirect:/admin/roles/{roleId}";
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
