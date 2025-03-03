package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.admin.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final AdminService adminService;

    /** 역할 목록 조회 페이지 */
    public String roleList() {
        return null;
    }

    /** 역할 상세 조회 페이지 */
    @GetMapping("/{roleId}")
    public String roleDetails(@PathVariable Long rolerId) {
        return null;
    }

    /** 역할 등록 페이지 */
    @GetMapping("/{roleId}/create")
    public String roleCreate(@PathVariable Long rolerId) {
        return null;
    }

    /** 역할 수정 페이지 */
    @GetMapping("/{roleId}/edit")
    public String userUpdate(@PathVariable Long rolerId) {
        return null;
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
