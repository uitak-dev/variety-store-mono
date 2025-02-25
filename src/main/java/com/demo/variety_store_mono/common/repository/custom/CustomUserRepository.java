package com.demo.variety_store_mono.common.repository.custom;

import com.demo.variety_store_mono.common.entity.User;
import com.demo.variety_store_mono.common.entity.UserType;
import com.demo.variety_store_mono.common.request.UserSearch;
import com.demo.variety_store_mono.common.response.SimpleUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomUserRepository {

    /** 사용자 목록 조회 */
    Page<SimpleUserInfo> searchUserListByUserType(UserType userType, UserSearch userSearch, Pageable pageable);

}
