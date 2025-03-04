package com.demo.variety_store_mono.admin.response;

import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetailResponse extends UserBasicInfoResponse {

    private String department;
}
