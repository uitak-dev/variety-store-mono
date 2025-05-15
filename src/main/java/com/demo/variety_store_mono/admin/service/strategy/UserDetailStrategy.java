package com.demo.variety_store_mono.admin.service.strategy;

import com.demo.variety_store_mono.common.dto.response.TokenResponse;

public interface UserDetailStrategy {

    /** 상세 페이지 뷰 이름 반환 */
    String getDetailView();

    /** 수정 페이지 뷰 이름 반환 */
    String getEditView();

    /** 사용자 상세 정보를 조회 */
    Object getDetail(Long userId);

    /** 사용자 상세 정보를 업데이트 (업데이트 요청 DTO 를 받아 처리) */
    void updateDetail(Long userId, Object request);

    /** 사용자 프로필 정보 조회 */
    Object getProfile(Long userId);

    /** 사용자 프로필 정보 수정 */
    void updateProfile(Long userId, Object request);

    /** 사용자 아이디 변경 */
    TokenResponse updateUserName(Long userId, String newUserName, String password);
}
