package com.demo.variety_store_mono.admin.entity;

import com.demo.variety_store_mono.seller.entity.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // 부모 카테고리 (self-referencing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 자식 카테고리 (self-referencing)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children = new LinkedHashSet<>();

    // 상품과의 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    // 옵션과의 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryGlobalOption> categoryGlobalOptions = new LinkedHashSet<>();

    @Builder
    public Category(Long id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.setParent(parent);
    }

    /** 카테고리 정보 수정 */
    public void update(String name, Category parent) {
        this.name = name;
        this.setParent(parent);
    }

    /** 부모-자식 관계 형성 */
    public void setParent(Category parent) {
        if (this.parent == parent) return;

        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }
}
