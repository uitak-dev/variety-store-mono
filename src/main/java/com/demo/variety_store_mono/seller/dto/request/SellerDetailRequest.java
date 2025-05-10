package com.demo.variety_store_mono.seller.dto.request;

import com.demo.variety_store_mono.common.dto.request.UserBasicInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailRequest extends UserBasicInfoRequest {

    private String companyName;
    private String businessLicenseNumber;
}
