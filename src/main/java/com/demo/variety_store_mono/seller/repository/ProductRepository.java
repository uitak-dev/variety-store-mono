package com.demo.variety_store_mono.seller.repository;

import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.entity.ProductOptionValue;
import com.demo.variety_store_mono.seller.entity.ProductStatus;
import com.demo.variety_store_mono.seller.repository.custom.CustomProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    // 판매자 도메인 - 상품 상세 조회.
    @Query("select distinct p from Product p " +
            "left join fetch p.productCategories pc " +
            "left join fetch pc.category c " +
            "left join fetch p.productOptions po " +
            "left join fetch po.productOptionValues pov " +
            "where p.seller.id = :sellerId " +
            "and p.id = :productId " +
            "order by po.id asc, pov.id asc")
    Optional<Product> findProductDetails(@Param("sellerId") Long sellerId, @Param("productId") Long productId);

    // 관리자 도메인 - 상품 상세 조회.
    @Query("select distinct p from Product p " +
            "left join fetch p.productCategories pc " +
            "left join fetch pc.category c " +
            "left join fetch p.productOptions po " +
            "left join fetch po.productOptionValues pov " +
            "left join fetch p.seller s " +
            "left join fetch s.user u " +
            "where p.id = :productId")
    Optional<Product> findProductManagementDetails(@Param("productId") Long productId);

    // 소비자 도메인 - 상품 상세 조회.
    @Query("select distinct p from Product p " +
            "left join fetch p.productOptions po " +
            "left join fetch po.productOptionValues pov " +
            "left join fetch p.seller s " +
            "left join fetch s.user u " +
            "where p.id = :productId " +
                "and p.status = ProductStatus.APPROVED " +
                "or p.status = ProductStatus.OUT_OF_STOCK")
    Optional<Product> findProductCatalogDetails(@Param("productId") Long productId);

    @Query("select pov from ProductOptionValue pov where pov.id = :optionValueId")
    Optional<ProductOptionValue> findProductOptionValueById(@Param("optionValueId") Long optionValueId);
}
