package com.demo.variety_store_mono.admin.request;

import com.demo.variety_store_mono.common.request.UserInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 관리자 상세 정보 수정 dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoRequest {

    private UserInfoRequest userBasicInfo;
    private String department;
}
