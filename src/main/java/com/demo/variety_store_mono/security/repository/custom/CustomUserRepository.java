package com.demo.variety_store_mono.security.repository.custom;

import com.demo.variety_store_mono.admin.dto.search.SearchUser;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.common.dto.response.UserBasicInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {

    /** 사용자 목록 조회 */
    Page<UserBasicInfoResponse> searchUserListByUserType(UserType userType, SearchUser searchUser, Pageable pageable);

}
