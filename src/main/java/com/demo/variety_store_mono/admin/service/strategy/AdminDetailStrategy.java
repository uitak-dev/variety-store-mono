package com.demo.variety_store_mono.admin.service.strategy;

import com.demo.variety_store_mono.admin.dto.request.AdminDetailRequest;
import com.demo.variety_store_mono.admin.dto.response.AdminDetailResponse;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.common.dto.form.AdminProfileForm;
import com.demo.variety_store_mono.common.dto.response.TokenResponse;
import com.demo.variety_store_mono.security.entity.RefreshToken;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.security.jwt.JwtTokenProvider;
import com.demo.variety_store_mono.security.repository.RefreshTokenRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminDetailStrategy implements UserDetailStrategy {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public AdminProfileForm getProfile(Long userId) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        AdminProfileForm ret = modelMapper.map(user, AdminProfileForm.class);
        ret.setDepartment(user.getAdmin().getDepartment());

        return ret;
    }

    @Override
    public void updateProfile(Long userId, Object adminProfileForm) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        AdminProfileForm request = (AdminProfileForm) adminProfileForm;

        // 사용자 기본 정보 변경.
        user.updateProfile(request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getPhoneNumber());

        // 관리자 상세 정보 변경.
        user.updateAdminDetail(request.getDepartment());
    }

    @Override
    public TokenResponse updateUserName(Long userId, String newUserName, String password) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 올바르지 않습니다.");
        }

        if (userRepository.existsByUserName(newUserName)) {
            throw new IllegalArgumentException("중복된 아이디 입니다.");
        }

        // 아이디 수정.
        user.updateUserName(newUserName);

        // JWT 재발급.
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList(),
                        "id", user.getId(),
                        "userType", user.getUserType())
        );

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList(),
                        "id", user.getId(),
                        "userType", user.getUserType())
        );

        RefreshToken refreshToken = user.getRefreshToken();
        refreshToken.update(newRefreshToken,
                Instant.now().plusMillis(jwtProperties.getRefreshTokenValidityMillis()));

        return new TokenResponse(accessToken, newRefreshToken);
    }
}
