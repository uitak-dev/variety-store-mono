package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.dto.request.RoleRequest;
import com.demo.variety_store_mono.admin.dto.search.SearchRole;
import com.demo.variety_store_mono.admin.dto.response.RoleResponse;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    /** 역할 생성 */
    public RoleResponse createRole(RoleRequest request) {

        Optional<Role> findRole = roleRepository.findByName(request.getName());
        if (findRole.isPresent()) {
            throw new RuntimeException("역할이 이미 존재합니다.");
        }

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return modelMapper.map(roleRepository.save(role), RoleResponse.class);
    }

    /** 역할 전체 조회 */
    public List<RoleResponse> getRoles() {
        return roleRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .collect(Collectors.toList());
    }

    /** 역할 목록 검색 조회 */
    @Transactional(readOnly = true)
    public Page<RoleResponse> getRoleSearchList(SearchRole searchRole, Pageable pageable) {
        return roleRepository.searchRoleList(searchRole, pageable);
    }

    /** 역할 상세 조회 */
    @Transactional(readOnly = true)
    public RoleResponse getRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("역할이 존재하지 않습니다."));

        return modelMapper.map(role, RoleResponse.class);
    }

    /** 역할 수정 */
    public void update(Long roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("역할이 존재하지 않습니다."));

        role.update(request.getName(), request.getDescription());
    }

    /** 역할 삭제 */

}
