package com.demo.variety_store_mono.admin.service.strategy;

import com.demo.variety_store_mono.admin.dto.request.AdminDetailRequest;
import com.demo.variety_store_mono.admin.dto.response.AdminDetailResponse;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminDetailStrategy implements UserDetailStrategy {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getDetailView() {
        return "admin/content/users/admin-detail";
    }

    @Override
    public String getEditView() {
        return "admin/content/users/admin-edit";
    }

    @Override
    public AdminDetailResponse getDetail(Long userId) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, AdminDetailResponse.class);
    }

    @Override
    public void updateDetail(Long userId, Object adminDetailRequest) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        AdminDetailRequest request = (AdminDetailRequest) adminDetailRequest;

        // 사용자에게 할당된 역할 변경.
        Set<Long> roleIds = request.getRoles();
        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.updateRoles(newRoles);

        // 사용자 기본 정보 변경.
        user.updateBasicInfo(request.getEmail(), request.getFirstName(), request.getLastName());

        // 관리자 상세 정보 변경.
        user.updateAdminDetail(request.getDepartment());
    }
}
