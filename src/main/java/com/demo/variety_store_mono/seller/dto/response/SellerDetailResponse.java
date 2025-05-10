package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.common.dto.response.UserBasicInfoResponse;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailResponse extends UserBasicInfoResponse{

    private String companyName;
    private String businessLicenseNumber;
}
