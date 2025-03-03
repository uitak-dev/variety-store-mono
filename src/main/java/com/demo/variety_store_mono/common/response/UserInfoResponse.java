package com.demo.variety_store_mono.common.response;

import com.demo.variety_store_mono.common.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
