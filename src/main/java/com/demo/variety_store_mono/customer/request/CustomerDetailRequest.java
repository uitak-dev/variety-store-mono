package com.demo.variety_store_mono.customer.request;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.request.UserBasicInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailRequest extends UserBasicInfoRequest {

//    private UserBasicInfoRequest userBasicInfo;

    private Address address;

}
