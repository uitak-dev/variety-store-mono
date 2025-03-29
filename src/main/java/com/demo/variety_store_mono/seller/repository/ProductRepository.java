package com.demo.variety_store_mono.seller.repository;

import com.demo.variety_store_mono.seller.entity.Product;
import com.demo.variety_store_mono.seller.repository.custom.CustomProductRepository;
import com.demo.variety_store_mono.seller.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    @Query("select distinct p from Product p " +
            "left join fetch p.productCategories pc " +
            "left join fetch pc.category c " +
            "left join fetch p.productOptions po " +
            "left join fetch po.globalOption go " +
            "left join fetch po.productOptionValues pov " +
            "left join fetch pov.globalOptionValue gov " +
            "where p.seller.id = :sellerId " +
            "and p.id = :productId")
    Optional<Product> findProductDetailByIdAndSellerId(@Param("sellerId") Long sellerId, @Param("productId") Long productId);
}
