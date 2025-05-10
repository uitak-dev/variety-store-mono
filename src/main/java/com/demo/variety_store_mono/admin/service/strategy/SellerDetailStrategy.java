package com.demo.variety_store_mono.admin.service.strategy;

import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.common.dto.form.AdminProfileForm;
import com.demo.variety_store_mono.common.dto.form.SellerProfileForm;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.admin.repository.RoleRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.seller.dto.request.SellerDetailRequest;
import com.demo.variety_store_mono.seller.dto.response.SellerDetailResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerDetailStrategy implements UserDetailStrategy {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String getDetailView() {
        return "admin/content/users/seller-detail";
    }

    @Override
    public String getEditView() {
        return "admin/content/users/seller-edit";
    }

    @Override
    public SellerDetailResponse getDetail(Long userId) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, SellerDetailResponse.class);
    }

    @Override
    public void updateDetail(Long userId, Object sellerDetailRequest) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        SellerDetailRequest request = (SellerDetailRequest) sellerDetailRequest;

        // 사용자에게 할당된 역할 변경.
        Set<Long> roleIds = request.getRoles();
        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.updateRoles(newRoles);

        // 사용자 기본 정보 변경.
        user.updateBasicInfo(request.getEmail(), request.getFirstName(), request.getLastName());

        // 판매자 상세 정보 변경.
        user.updateSellerDetail(request.getCompanyName(), request.getBusinessLicenseNumber());
    }

    @Override
    public SellerProfileForm getProfile(Long userId) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        SellerProfileForm ret = modelMapper.map(user, SellerProfileForm.class);
        ret.setCompanyName(user.getSeller().getCompanyName());
        ret.setBusinessLicenseNumber(user.getSeller().getBusinessLicenseNumber());

        return ret;
    }

    @Override
    public void updateProfile(Long userId, Object sellerProfileForm) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        SellerProfileForm request = (SellerProfileForm) sellerProfileForm;

        // 사용자 기본 정보 변경.
        user.updateProfile(request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getPhoneNumber());

        // 판매자 상세 정보 변경.
        user.updateSellerDetail(request.getCompanyName(), request.getBusinessLicenseNumber());
    }

    @Override
    public void updateUserName(Long userId, String newUserName, String password) {
        User user = userRepository.findUserDetailsById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 올바르지 않습니다.");
        }

        if (userRepository.existsByUserName(newUserName)) {
            throw new IllegalArgumentException("중복된 아이디 입니다.");
        }

        user.updateUserName(newUserName);
    }
}
