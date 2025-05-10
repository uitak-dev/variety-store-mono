package com.demo.variety_store_mono.admin.dto.response;

import com.demo.variety_store_mono.common.dto.response.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailResponse extends UserBasicInfoResponse {

    private String department;
}
