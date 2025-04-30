package com.demo.variety_store_mono.admin.controller.web;

import com.demo.variety_store_mono.admin.dto.request.RoleRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchRole;
import com.demo.variety_store_mono.admin.dto.response.RoleResponse;
import com.demo.variety_store_mono.admin.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleManagementController {

    private final RoleService roleService;

    /** 역할 목록 조회 페이지 */
    @GetMapping
    public String roleList(@ModelAttribute SearchRole searchRole, Model model,
                           @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

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
    public String newRole(@ModelAttribute("role") @Valid RoleRequest request, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes) {

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "admin/content/roles/role-new";
        }

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
    public String updateRole(@PathVariable Long roleId, @ModelAttribute("role") @Valid RoleRequest request,
                             BindingResult bindingResult) {

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "admin/content/roles/role-edit";
        }

        roleService.update(roleId, request);
        return "redirect:/admin/roles/{roleId}";
    }
}
