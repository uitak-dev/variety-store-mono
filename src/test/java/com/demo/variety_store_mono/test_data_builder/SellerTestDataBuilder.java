package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;
import com.demo.variety_store_mono.seller.entity.Seller;

public class SellerTestDataBuilder {

    private Long id;
    private User user;

    public SellerTestDataBuilder() {
        this.user = defaultUser();
    }

    private User defaultUser() {
        User user = new UserTestDataBuilder(UserType.SELLER).build();
        this.id = user.getId();
        return user;
    }

    public static SellerTestDataBuilder builder() {
        return new SellerTestDataBuilder();
    }

    public SellerTestDataBuilder withUser(User user) {
        this.user = user;
        this.id = user.getId();
        return this;
    }

    public Seller build() {
        return Seller.builder()
                .user(user)
                .build();
    }
}
