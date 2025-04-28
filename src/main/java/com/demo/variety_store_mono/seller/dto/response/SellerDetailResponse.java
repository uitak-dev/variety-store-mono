package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.common.response.RoleResponse;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailResponse extends UserBasicInfoResponse{

    private String companyName;
    private String businessLicenseNumber;
}
