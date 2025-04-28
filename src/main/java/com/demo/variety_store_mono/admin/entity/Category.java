package com.demo.variety_store_mono.admin.entity;

import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.security.entity.UserRole;
import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends Audit {

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
    @OrderBy("id desc")
    private Set<Category> children = new LinkedHashSet<>();

    // 상품과의 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    // 옵션과의 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private Set<CategoryGlobalOption> categoryGlobalOptions = new LinkedHashSet<>();

    @Builder
    public Category(Long id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.setParent(parent);
    }

    /** * * * * * * * * * * * * * * * *
     * association convenience method *
     * * * * * * * * * * * * * * * * */

    // 카테고리-상품 연관관계 편의 메서드.
    public void addProduct(Product product) {
        ProductCategory productCategory = new ProductCategory(product, this);
        productCategories.add(productCategory);
        product.getProductCategories().add(productCategory);
    }

    /** 카테고리 정보 수정 */
    public void update(String name, Category parent) {
        this.name = name;
        this.setParent(parent);
    }

    /** 부모-자식 연관관계 형성 */
    private void setParent(Category parent) {
        if (this.parent == parent) return;

        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }

    /** 카테고리-옵션 연관관계 편의 메서드 */
    public void updateGlobalOption(Set<GlobalOption> newGlobalOptions) {

        if (newGlobalOptions == null) {
            throw new IllegalArgumentException("매개변수가 null 값이면 안됩니다.");
        }

        // 현재 categoryGlobalOptions 에서 GlobalOption 목록 추출
        Set<GlobalOption> existingOptions = categoryGlobalOptions.stream()
                .map(CategoryGlobalOption::getGlobalOption)
                .collect(Collectors.toSet());

        // 삭제할 옵션: 기존 옵션 목록 중에서 인자로 받은 옵션 목록에 없는 역할.
        Set<GlobalOption> optionsToRemove = new HashSet<>(existingOptions);
        optionsToRemove.removeAll(newGlobalOptions);

        // 추가할 옵션: 인자로 받은 옵션 목록 중에서 기존 옵션 목록에 없는 역할.
        Set<GlobalOption> optionsToAdd = new HashSet<>(newGlobalOptions);
        optionsToAdd.removeAll(existingOptions);

        // 옵션 삭제 (Iterator 사용)
        Iterator<CategoryGlobalOption> iterator = categoryGlobalOptions.iterator();
        while (iterator.hasNext()) {
            CategoryGlobalOption categoryGlobalOption = iterator.next();
            if (optionsToRemove.contains(categoryGlobalOption.getGlobalOption())) {
                iterator.remove();
            }
        }

        // 옵션 추가
        optionsToAdd.forEach(globalOption ->
            categoryGlobalOptions.add(new CategoryGlobalOption(this, globalOption))
        );
    }

    /** 카테고리에 등록된 옵션 템플릿 조회 */
    public Set<GlobalOption> getGlobalOptions() {
        return categoryGlobalOptions.stream()
                .map(CategoryGlobalOption::getGlobalOption)
                .collect(Collectors.toSet());
    }
}
