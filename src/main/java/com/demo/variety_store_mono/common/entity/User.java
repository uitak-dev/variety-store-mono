package com.demo.variety_store_mono.common.entity;

import com.demo.variety_store_mono.admin.entity.AdminDetail;
import com.demo.variety_store_mono.customer.entity.CustomerDetail;
import com.demo.variety_store_mono.seller.entity.SellerDetail;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String email;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;      // CUSTOMER, SELLER, ADMIN

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerDetail customerDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SellerDetail sellerDetail;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdminDetail adminDetail;

    @Builder
    public User(Long id, String userName, String password,
                String email, String firstName, String lastName,
                String phoneNumber, UserType userType) {

        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public Set<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
    }

    public void updateBasicInfo(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void createAdminDetail() {
        this.adminDetail = AdminDetail.builder().user(this).build();
    }

    public void updateAdminDetail(String department) {
        this.getAdminDetail().updateInfo(department);
    }

    public void createSellerDetail() {
        this.sellerDetail = SellerDetail.builder().user(this).build();
    }

    public void updateSellerDetail(String companyName, String businessLicenseNumber) {
        this.getSellerDetail().updateInfo(companyName, businessLicenseNumber);
    }

    public void createCustomerDetail() {
        this.customerDetail = CustomerDetail.builder().user(this).build();
    }

    public void updateCustomerDetail(Address address) {
        this.getCustomerDetail().updateInfo(address);
    }

    public void addRole(Role role) {
        userRoles.add(new UserRole(this, role));
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }
}
