package com.demo.variety_store_mono.admin.response;

import com.demo.variety_store_mono.common.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoResponse {

    private UserInfoResponse userBasicInfo;

    private String department;
}
