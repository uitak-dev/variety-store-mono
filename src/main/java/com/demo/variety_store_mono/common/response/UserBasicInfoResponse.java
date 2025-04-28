package com.demo.variety_store_mono.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBasicInfoResponse {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private Set<RoleResponse> roles;
}
