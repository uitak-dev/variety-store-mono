package com.demo.variety_store_mono.seller.response;

import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailResponse extends UserBasicInfoResponse{

    private String companyName;
    private String businessLicenseNumber;
}
