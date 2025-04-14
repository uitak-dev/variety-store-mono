package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.security.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    private Long id; // User와 동일한 ID 사용 (Foreign Key)

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderDate desc")
    Set<Order> orders = new LinkedHashSet<>();

    @Builder
    public Customer(User user, Address address) {
        this.user = user;
        this.address = address;
    }

    public void updateInfo(Address address) {
        this.address = address;
    }

    void assignCart(Cart cart) {
        this.cart = cart;
    }
}
