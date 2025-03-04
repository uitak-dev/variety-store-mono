package com.demo.variety_store_mono.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoResponse {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
