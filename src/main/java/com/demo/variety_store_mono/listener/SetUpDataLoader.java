package com.demo.variety_store_mono.listener;

import com.demo.variety_store_mono.common.entity.Role;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.repository.RoleRepository;
import com.demo.variety_store_mono.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    // 스레드 안전한 중복 실행 방지 플래그
    private final AtomicBoolean alreadySetup = new AtomicBoolean(false);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup.get()) {
            return;
        }
        setupData();
        alreadySetup.set(true);
    }

    private void setupData() {
        int cnt = 0;

        // 관리자 계정 생성.
        for (int i = 0; i < 7; i++) {
            createUserIfNotFound("admin" + String.format("%03d", ++cnt), "qwer1234",
                    "admin" + String.format("%03d", cnt) + "@example.com", UserType.ADMIN);
        }

        // 판매자 계정 생성.
        for (int i = 0; i < 19; i++) {
            createUserIfNotFound("seller" + String.format("%03d", ++cnt), "qwer1234",
                    "seller" + String.format("%03d", cnt) + "@example.com", UserType.SELLER);
        }

        // 일반 사용자 계정 생성.
        for (int i = 0; i < 54; i++) {
            createUserIfNotFound("user" + String.format("%03d", ++cnt), "qwer1234",
                    "user" + String.format("%03d", cnt) + "@example.com", UserType.CUSTOMER);
        }
    }

    private void createUserIfNotFound(String userName, String password, String email, UserType userType) {

        Optional<User> existingMember = userRepository.findByUserName(userName);
        if (existingMember.isPresent()) return;

        User newUser = User.builder()
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .email(email)
                .userType(userType)
                .build();

        switch (userType.name()) {
            case "ADMIN" -> newUser.createAdminDetail();
            case "SELLER" -> newUser.createSellerDetail();
            case "CUSTOMER" -> newUser.createCustomerDetail();
        }

        String description = switch (userType.name()) {
            case "ADMIN" -> "관리자";
            case "SELLER" -> "판매자";
            case "CUSTOMER" -> "일반 사용자";
            default -> "";
        };

        Role role = createRoleIfNotFound(userType.getRoleName(), description);
        newUser.addRole(role);

        userRepository.save(newUser);
    }

    private Role createRoleIfNotFound(String roleName, String description) {

        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            return existingRole.get();
        }

        Role newRole = Role.builder()
                .name(roleName)
                .description(description)
                .build();

        return roleRepository.save(newRole);
    }
}
