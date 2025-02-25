package com.demo.variety_store_mono.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    private Long refreshTokenId;
    private String refreshToken;
}
