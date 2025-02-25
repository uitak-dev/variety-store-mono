package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.common.entity.Role;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.repository.RoleRepository;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /** 관리자 상세정보 등록 */
    public void createAdmin(SignUpRequest request) {

        User admin = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // 관리자 기본 역할 할당.
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");

        Role adminRole;
        if (optionalRole.isPresent()){
            adminRole = optionalRole.get();
        } else {
            adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .description("관리자")
                    .build();

            adminRole = roleRepository.save(adminRole);
        }

        admin.addRole(adminRole);
        admin = userRepository.save(admin);
    }

    /** 관리자 상세 조회
    public AdminResponse getAdminById(Long adminId) {
        return userRepository.findUserDetailsByUserName(adminId)
                .map(admin -> modelMapper.map(admin, AdminResponse.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
     */

    /** 관리자 수정 */

    /** 관리자 삭제 */
}
