package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.customer.dto.summary.ProductCatalogSummary;
import com.demo.variety_store_mono.customer.entity.CartItemOption;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;
    private ProductCatalogSummary product;
    private int quantity;
    private List<CartItemOptionResponse> cartItemOptions;
    private BigDecimal unitPrice;
}
