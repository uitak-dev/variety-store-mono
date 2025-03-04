package com.demo.variety_store_mono.common.service;

import com.demo.variety_store_mono.common.entity.Role;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.repository.RoleRepository;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.SignUpRequest;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    /** 사용자 등록 */
    public void createUser(SignUpRequest request, UserType userType) {

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .userType(userType)
                .build();

        switch (userType.name()) {
            case "ADMIN" -> user.createAdminDetail();
            case "SELLER" -> user.createSellerDetail();
            case "CUSTOMER" -> user.createCustomerDetail();
        }

        // 기본 역할 할당.
        Role role = roleRepository.findByName(userType.getRoleName())
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(userType.getRoleName())
                                .build()
                ));

        user.addRole(role);
        userRepository.save(user);
    }

    /** 사용자 기본 정보 수정 */
    public void updateUserBasicInfo(Long userId, UserBasicInfoRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        user.updateBasicInfo(request.getEmail(), request.getFirstName(), request.getLastName());
    }
}
