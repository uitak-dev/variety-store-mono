package com.demo.variety_store_mono.customer.response;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoResponse {

    private UserInfoResponse userBasicInfo;

    private Address address;
}
