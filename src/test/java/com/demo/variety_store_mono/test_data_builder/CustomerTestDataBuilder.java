package com.demo.variety_store_mono.test_data_builder;

import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.entity.UserType;

public class CustomerTestDataBuilder {

    private Long id;
    private User user;
    private Address address;

    public CustomerTestDataBuilder() {
        this.user = defaultUser();
        this.address = defaultAddress();
    }

    private User defaultUser() {
        User user = new UserTestDataBuilder(UserType.CUSTOMER).build();
        this.id = user.getId();
        return user;
    }

    private Address defaultAddress() {
        return Address.builder()
                .state("서울특별시")
                .city("강남구")
                .area("대치동")
                .street("테헤란로")
                .buildingNumber("123")
                .apartment("101동 1001호")
                .zipCode("12-123")
                .build();
    }

    public static CustomerTestDataBuilder builder() {
        return new CustomerTestDataBuilder();
    }

    public CustomerTestDataBuilder withUser(User user) {
        this.user = user;
        this.id = user.getId();
        return this;
    }

    public CustomerTestDataBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public Customer build() {
        return Customer.builder()
                .user(user)
                .address(address)
                .build();
    }
}
