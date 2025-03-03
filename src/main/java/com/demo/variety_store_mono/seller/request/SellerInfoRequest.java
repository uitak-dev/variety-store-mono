package com.demo.variety_store_mono.seller.request;

import com.demo.variety_store_mono.common.request.UserInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerInfoRequest {

    private UserInfoRequest userBasicInfo;

    private String companyName;
    private String businessLicenseNumber;
}
