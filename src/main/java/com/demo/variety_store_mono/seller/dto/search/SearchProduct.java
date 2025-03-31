package com.demo.variety_store_mono.seller.dto.search;

import com.demo.variety_store_mono.seller.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SearchProduct {

    String name;
    ProductStatus status;
}
