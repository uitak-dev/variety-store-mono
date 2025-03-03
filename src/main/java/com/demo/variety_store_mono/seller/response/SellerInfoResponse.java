package com.demo.variety_store_mono.seller.response;

import com.demo.variety_store_mono.common.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerInfoResponse {

    private UserInfoResponse userBasicInfo;

    private String companyName;
    private String businessLicenseNumber;
}
