package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.admin.entity.Category;
import com.demo.variety_store_mono.seller.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"product", "category"}, callSuper = false)
@Table(
        name = "product_category",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "category_id"})
)
public class ProductCategory {

    @Id
    @GeneratedValue
    @Column(name = "product_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
    }
}
