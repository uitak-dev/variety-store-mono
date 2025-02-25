package com.demo.variety_store_mono.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {

    private Long id;
    private String username;
    private String email;
}
