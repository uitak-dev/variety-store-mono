package com.demo.variety_store_mono.seller.repository;

import com.demo.variety_store_mono.seller.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
