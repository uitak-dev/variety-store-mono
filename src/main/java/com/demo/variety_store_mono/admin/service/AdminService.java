package com.demo.variety_store_mono.admin.service;

import com.demo.variety_store_mono.admin.response.AdminDetailResponse;
import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.repository.UserRepository;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import com.demo.variety_store_mono.admin.request.UserSearch;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import com.demo.variety_store_mono.common.service.UserService;
import com.demo.variety_store_mono.customer.request.CustomerDetailRequest;
import com.demo.variety_store_mono.customer.response.CustomerDetailResponse;
import com.demo.variety_store_mono.seller.request.SellerDetailRequest;
import com.demo.variety_store_mono.seller.response.SellerDetailResponse;
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
    public Page<UserBasicInfoResponse> getUserList(UserType userType, UserSearch userSearch, Pageable pageable) {
        return userRepository.searchUserListByUserType(userType, userSearch, pageable);
    }


    // ==== RoleService ====
    /** 역할 목록 조회 */

    /** 역할 상세 조회 */

    /** 역할 등록, 수정, 삭제 */
}
