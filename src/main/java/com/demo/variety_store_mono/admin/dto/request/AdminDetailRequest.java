package com.demo.variety_store_mono.admin.dto.request;

import com.demo.variety_store_mono.common.dto.request.UserBasicInfoRequest;
import jakarta.validation.constraints.Size;
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

    @Size(max = 30, message = "부서명은 최대 30자 까지 입력 가능합니다.")
    private String department;
}
