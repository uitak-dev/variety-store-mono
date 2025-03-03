package com.demo.variety_store_mono.customer.request;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.request.UserInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoRequest {

    private UserInfoRequest userBasicInfo;

    private Address address;
}
