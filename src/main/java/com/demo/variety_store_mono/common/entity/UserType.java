package com.demo.variety_store_mono.common.entity;

public enum UserType {
    ADMIN("ROLE_ADMIN"), SELLER("ROLE_SELLER"), CUSTOMER("ROLE_CUSTOMER");

    private final String roleName;

    UserType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
