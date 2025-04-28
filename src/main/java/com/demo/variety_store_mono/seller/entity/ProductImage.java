package com.demo.variety_store_mono.seller.entity;

import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.common.entity.UploadFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "product_image_id")
    private Long id;

    @Embedded
    private UploadFile uploadFile;

    @Column(nullable = false)
    private boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private ProductImage(UploadFile uploadFile, boolean isThumbnail) {
        this.uploadFile = uploadFile;
        this.isThumbnail = isThumbnail;
    }

    void assignProduct(Product product) {
        this.product = product;
    }

    public void setThumbnail(boolean thumbnail) {
        this.isThumbnail = thumbnail;
    }
}
