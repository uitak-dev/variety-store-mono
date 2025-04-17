package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.seller.entity.ProductOptionValue;

import java.math.BigDecimal;

public class ProductOptionValueTestDataBuilder {

    private Long id;
    private String optionValue;
    private boolean global;
    private BigDecimal additionalPrice;
    private int stockQuantity;

    public ProductOptionValueTestDataBuilder() {
        this.optionValue = "Default Option Value";
        this.global = false;
        this.additionalPrice = BigDecimal.valueOf(500);
        this.stockQuantity = 20;
    }

    public static ProductOptionValueTestDataBuilder builder() {
        return new ProductOptionValueTestDataBuilder();
    }

    public ProductOptionValueTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductOptionValueTestDataBuilder withOptionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    public ProductOptionValueTestDataBuilder withGlobal(boolean global) {
        this.global = global;
        return this;
    }

    public ProductOptionValueTestDataBuilder withAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
        return this;
    }

    public ProductOptionValueTestDataBuilder withStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }

    public ProductOptionValue build() {
        return ProductOptionValue.builder()
                .id(id)
                .optionValue(optionValue)
                .global(global)
                .additionalPrice(additionalPrice)
                .stockQuantity(stockQuantity)
                .build();
    }
}
