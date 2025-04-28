package com.demo.variety_store_mono.admin.entity;

import com.demo.variety_store_mono.common.entity.Audit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryGlobalOption extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "category_global_option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "global_option_id")
    private GlobalOption globalOption;

    public CategoryGlobalOption(Category category, GlobalOption globalOption) {
        this.category = category;
        this.globalOption = globalOption;
    }
}
