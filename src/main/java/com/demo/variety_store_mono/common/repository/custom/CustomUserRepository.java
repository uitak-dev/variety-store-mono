package com.demo.variety_store_mono.common.repository.custom;

import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.admin.request.UserSearch;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {

    /** 사용자 목록 조회 */
    Page<UserBasicInfoResponse> searchUserListByUserType(UserType userType, UserSearch userSearch, Pageable pageable);

}
