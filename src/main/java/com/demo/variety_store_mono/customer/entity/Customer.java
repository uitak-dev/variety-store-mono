package com.demo.variety_store_mono.customer.entity;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.security.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Customer(User user, Address address) {
        this.user = user;
        this.address = address;
    }

    public void updateInfo(Address address) {
        this.address = address;
    }
}
