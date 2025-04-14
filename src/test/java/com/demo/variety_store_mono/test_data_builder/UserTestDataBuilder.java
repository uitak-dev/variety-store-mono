package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;

public class UserTestDataBuilder {

    private Long id;
    private String userName;
    private UserType userType;

    public UserTestDataBuilder(UserType userType) {
        this.userName = "testuser";
        this.userType = userType;
    }

    public static UserTestDataBuilder builder(UserType userType) {
        return new UserTestDataBuilder(userType);
    }

    public UserTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserTestDataBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserTestDataBuilder withUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public User build() {
        return User.builder()
                .id(id)
                .userName(userName)
                .userType(userType)
                .build();
    }
}
