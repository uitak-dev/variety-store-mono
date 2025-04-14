package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProductOptionTestDataBuilder {

    private Long id;
    private boolean global;
    private String name;
    private Set<ProductOptionValue> productOptionValues = new LinkedHashSet<>();

    public ProductOptionTestDataBuilder() {
        this.global = false;
        this.name = "Test Product Option";
        this.productOptionValues = Set.of(new ProductOptionValueTestDataBuilder().build());
    }

    public static ProductOptionTestDataBuilder builder() {
        return new ProductOptionTestDataBuilder();
    }

    public ProductOptionTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductOptionTestDataBuilder withGlobal(boolean global) {
        this.global = global;
        return this;
    }

    public ProductOptionTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductOptionTestDataBuilder withProductOptionValues(Set<ProductOptionValue> optionValues) {
        this.productOptionValues = optionValues;
        return this;
    }

    public ProductOptionTestDataBuilder withProductOptionValue(ProductOptionValue optionValue) {
        productOptionValues.add(optionValue);
        return this;
    }

    public ProductOption build() {
        ProductOption productOption = ProductOption.builder()
                .id(id)
                .global(global)
                .name(name)
                .build();

        if (!productOptionValues.isEmpty()) {
            productOptionValues.forEach(productOption::addProductOptionValue);
        }

        return productOption;
    }
}
