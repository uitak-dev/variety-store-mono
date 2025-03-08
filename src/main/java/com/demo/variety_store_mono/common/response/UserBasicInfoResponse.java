package com.demo.variety_store_mono.common.response;

import com.demo.variety_store_mono.admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoResponse {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;

    private Set<Role> roles;
}
