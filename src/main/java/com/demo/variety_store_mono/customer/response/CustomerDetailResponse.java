package com.demo.variety_store_mono.customer.response;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailResponse extends UserBasicInfoResponse{

    private Address address;
}
