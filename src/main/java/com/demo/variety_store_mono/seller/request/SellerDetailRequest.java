package com.demo.variety_store_mono.seller.request;

import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailRequest extends UserBasicInfoRequest {

//    private UserBasicInfoRequest userBasicInfo;

    private String companyName;
    private String businessLicenseNumber;
}
