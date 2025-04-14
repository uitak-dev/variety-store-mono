package com.demo.variety_store_mono.customer.repository;

import com.demo.variety_store_mono.customer.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
