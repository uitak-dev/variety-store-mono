package com.demo.variety_store_mono.admin.service.strategy;

public interface UserDetailStrategy {

    /** 상세 페이지 뷰 이름 반환 */
    String getDetailView();

    /** 수정 페이지 뷰 이름 반환 */
    String getEditView();

    /** 사용자 상세 정보를 조회 */
    Object getDetail(Long userId);

    /** 사용자 상세 정보를 업데이트 (업데이트 요청 DTO 를 받아 처리) */
    void updateDetail(Long userId, Object request);
}
