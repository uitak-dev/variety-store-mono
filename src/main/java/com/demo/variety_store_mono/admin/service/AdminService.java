package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.request.AdminInfoRequest;
import com.demo.variety_store_mono.admin.response.AdminInfoResponse;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.UserInfoRequest;
import com.demo.variety_store_mono.admin.request.UserSearch;
import com.demo.variety_store_mono.common.response.UserInfoResponse;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.customer.request.CustomerInfoRequest;
import com.demo.variety_store_mono.customer.response.CustomerInfoResponse;
import com.demo.variety_store_mono.seller.request.SellerInfoRequest;
import com.demo.variety_store_mono.seller.response.SellerInfoResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /** 사용자 목록 조회 */
    public Page<UserInfoResponse> getUserList(UserType userType, UserSearch userSearch, Pageable pageable) {
        return userRepository.searchUserListByUserType(userType, userSearch, pageable);
    }

    /** 관리자 상세 조회 */
    public AdminInfoResponse getAdminInfo(Long userId) {

        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, AdminInfoResponse.class);
    }

    /** 판매자 상세 조회 */
    public SellerInfoResponse getSellerInfo(Long userId) {

        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, SellerInfoResponse.class);
    }

    /** 고객 상세 조회 */
    public CustomerInfoResponse getCustomerInfo(Long userId) {

        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return modelMapper.map(user, CustomerInfoResponse.class);
    }

    /** 관리자 상세 정보 수정 */
    public void updateAdminInfo(Long userId, AdminInfoRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 사용자 기본 정보 먼저 변경.
        UserInfoRequest userInfoRequest = request.getUserBasicInfo();
        userService.updateUserInfo(userId, userInfoRequest);

        // 관리자 상세 정보 변경.
        user.updateAdminDetail(request.getDepartment());
    }

    /** 판매자 상세 정보 수정 */
    public void updateSellerInfo(Long userId, SellerInfoRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 사용자 기본 정보 먼저 변경.
        UserInfoRequest userInfoRequest = request.getUserBasicInfo();
        userService.updateUserInfo(userId, userInfoRequest);

        // 판매자 상세 정보 변경.
        user.updateSellerDetail(request.getCompanyName(), request.getBusinessLicenseNumber());
    }

    /** 고객 상세 정보 수정 */
    public void updateCustomerInfo(Long userId, CustomerInfoRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 사용자 기본 정보 먼저 변경.
        UserInfoRequest userInfoRequest = request.getUserBasicInfo();
        userService.updateUserInfo(userId, userInfoRequest);

        // 판매자 상세 정보 변경.
        user.updateCustomerDetail(request.getAddress());
    }


    // ==== RoleService ====
    /** 역할 목록 조회 */

    /** 역할 상세 조회 */

    /** 역할 등록, 수정, 삭제 */
}
