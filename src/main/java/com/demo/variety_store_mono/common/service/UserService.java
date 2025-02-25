package com.demo.variety_store_mono.common.service;

import com.demo.variety_store_mono.common.entity.Role;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.repository.RoleRepository;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.SignUpRequest;
import com.demo.variety_store_mono.common.request.UserSearch;
import com.demo.variety_store_mono.common.response.SimpleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .userType(userType)
                .build();

        // 기본 역할 할당.
        Role role = roleRepository.findByName(userType.getRoleName())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(userType.getRoleName())
                        .build()));

        user.addRole(role);
        userRepository.save(user);
    }

    /** 사용자 목록 조회 */
    public Page<SimpleUserInfo> getUserList(UserType userType, UserSearch userSearch, Pageable pageable) {
        return userRepository.searchUserListByUserType(userType, userSearch, pageable);
    }
}
