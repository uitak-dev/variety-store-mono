package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.response.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailResponse extends UserBasicInfoResponse{

    private Address address;
}
