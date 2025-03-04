package com.demo.variety_store_mono.admin.request;

import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 관리자 상세 정보 수정 dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDetailRequest extends UserBasicInfoRequest {

    private String department;
}
