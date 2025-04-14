package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.security.entity.User;
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
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Customer customer;

    // 장바구니에 담긴 항목들
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    @Builder
    public Cart(Customer customer) {
        this.customer = customer;
        customer.assignCart(this);
    }

    // relation convenience methods
    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.assignCart(this);
    }
}
