package com.demo.variety_store_mono.seller.dto.response;

import com.demo.variety_store_mono.admin.dto.response.GlobalOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionResponse {

    // 옵션 이름
    private String name;

    // 해당 옵션에 대한 값들을 담은 리스트.
    private List<ProductOptionValueResponse> productOptionValues;
}
