package com.demo.variety_store_mono.customer.repository;

import com.demo.variety_store_mono.customer.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
