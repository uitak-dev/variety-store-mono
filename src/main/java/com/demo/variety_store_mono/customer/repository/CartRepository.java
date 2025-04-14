package com.demo.variety_store_mono.customer.repository;

import com.demo.variety_store_mono.customer.entity.Cart;
import com.demo.variety_store_mono.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c " +
            "left join fetch c.cartItems ci " +
            "left join fetch ci.cartItemOptions cio " +
            "where c.customer.id = :customerId")
    Optional<Cart> findByCustomerId(@Param("customerId") Long customerId);
}
