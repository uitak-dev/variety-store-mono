package com.demo.variety_store_mono.common.request;

import com.demo.variety_store_mono.common.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearch {

    private String userName;
    private String email;
}
