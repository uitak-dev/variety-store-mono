package com.demo.variety_store_mono.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    private String email;
    private String firstName;
    private String lastName;
}
