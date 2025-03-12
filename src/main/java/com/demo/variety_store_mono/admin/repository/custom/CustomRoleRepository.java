package com.demo.variety_store_mono.admin.repository.custom;

import com.demo.variety_store_mono.admin.request.SearchRole;
import com.demo.variety_store_mono.admin.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRoleRepository {

    /** 역할 목록 조회 */
    Page<RoleResponse> searchRoleList(SearchRole searchRole, Pageable pageable);
}
