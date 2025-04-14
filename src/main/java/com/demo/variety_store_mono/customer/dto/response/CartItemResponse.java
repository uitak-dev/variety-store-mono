package com.demo.variety_store_mono.customer.dto.response;

import com.demo.variety_store_mono.customer.entity.CartItemOption;
import com.demo.variety_store_mono.seller.dto.summary.ProductSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private List<ProductSummary> products;
    private int quantity;
    private List<CartItemOption> cartItemOptions;
}
