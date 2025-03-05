package com.demo.variety_store_mono.common.request;

import com.demo.variety_store_mono.common.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 회원 정보 수정 dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoRequest {

    private String email;
    private String firstName;
    private String lastName;

    private Set<Long> roles;
}
