package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOption;
import com.demo.variety_store_mono.seller.entity.Seller;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProductTestDataBuilder {

    private Long id;
    private Category primaryCategory;
    private String name;
    private BigDecimal basePrice;
    private int stockQuantity;
    private boolean single;
    private Seller seller;
    private Set<ProductOption> productOptions = new LinkedHashSet<>();

    public ProductTestDataBuilder() {
        this.primaryCategory = defaultCategory();
        this.name = "Test Product";
        this.basePrice = BigDecimal.valueOf(12900);
        this.stockQuantity = 100;
        this.single = true;
        this.seller = new SellerTestDataBuilder().build();
    }

    private Category defaultCategory() {
        return Category.builder()
                .id(1L)
                .name("Test Category")
                .parent(null)
                .build();
    }

    public static ProductTestDataBuilder builder() {
        return new ProductTestDataBuilder();
    }

    public ProductTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductTestDataBuilder withPrimaryCategory(Category category) {
        this.primaryCategory = category;
        return this;
    }

    public ProductTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductTestDataBuilder withBasePrice(BigDecimal price) {
        this.basePrice = price;
        return this;
    }

    public ProductTestDataBuilder withStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }

    public ProductTestDataBuilder withSingle(boolean single) {
        this.single = single;
        return this;
    }

    public ProductTestDataBuilder withSeller(Seller seller) {
        this.seller = seller;
        return this;
    }

    public ProductTestDataBuilder withProductOptions(Set<ProductOption> options) {
        this.productOptions = options;
        return this;
    }

    public ProductTestDataBuilder withProductOption(ProductOption option) {
        this.productOptions.add(option);
        return this;
    }

    public Product build() {
        Product product = Product.builder()
                .id(id)
                .primaryCategory(primaryCategory)
                .name(name)
                .single(single)
                .basePrice(basePrice)
                .stockQuantity(stockQuantity)
                .seller(seller)
                .build();

        if (!productOptions.isEmpty()) {
            productOptions.forEach(product::addProductOption);
        }

        return product;
    }
}
