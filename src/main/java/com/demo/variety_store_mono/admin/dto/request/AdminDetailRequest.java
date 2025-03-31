package com.demo.variety_store_mono.admin.dto.request;

import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 관리자 상세 정보 수정 dto
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AdminDetailRequest extends UserBasicInfoRequest {

    private String department;
}
