package com.demo.variety_store_mono.seller.response;

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

    // 판매자가 시스템에 정의된 옵션을 선택하는 경우, true
    // 판매자가 직접 정의한 옵션을 사용하는 경우, false
    private boolean isGlobal;

    // 시스템 옵션 사용 시: 선택된 (템플릿)옵션 id
    private Long globalOptionId;
    // 사용자 정의 옵션 사용 시: 옵션 이름
    private String productOptionName;

    // 해당 옵션에 대한 값들을 담은 리스트.
    private List<ProductOptionValueResponse> productOptionValues;
}
