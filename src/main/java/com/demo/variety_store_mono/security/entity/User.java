package com.demo.variety_store_mono.security.entity;

import com.demo.variety_store_mono.admin.entity.Admin;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.common.entity.Address;
import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.customer.entity.Customer;
import com.demo.variety_store_mono.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
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

    // 고객 상세 정보
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    // 판매자 상세 정보
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Seller seller;

    // 관리자 상세 정보
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;

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

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    /** 사용자 기본 정보 수정 */
    public void updateBasicInfo(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** 사용자에게 할당된 역할 조회 */
    public Set<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
    }

    /** 사용자 역할 수정 */
    public void updateRoles(Set<Role> newRoles) {
        // 현재 UserRole에서 Role 목록 추출
        Set<Role> existingRoles = userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());

        // 삭제할 역할: 기존 역할 목록 중에서 인자로 받은 역할 목록에 없는 역할.
        Set<Role> rolesToRemove = new HashSet<>(existingRoles);
        rolesToRemove.removeAll(newRoles);

        // 추가할 역할: 인자로 받은 역할 목록 중에서 기존 역할 목록에 없는 역할.
        Set<Role> rolesToAdd = new HashSet<>(newRoles);
        rolesToAdd.removeAll(existingRoles);

        // 역할 삭제 (Iterator 사용)
        Iterator<UserRole> iterator = userRoles.iterator();
        while (iterator.hasNext()) {
            UserRole userRole = iterator.next();
            if (rolesToRemove.contains(userRole.getRole())) {
                iterator.remove();
            }
        }

        // 역할 추가
        rolesToAdd.forEach(this::addRole);
    }

    /** 사용자에게 역할 추가 */
    public void addRole(Role role) {
        if (userRoles.stream().noneMatch(userRole -> userRole.getRole().equals(role))) {
            userRoles.add(new UserRole(this, role));
        }
    }

    /** 사용자에게 역할 제거 */
    public void removeRole(Long roleId) {
        userRoles.removeIf(userRole -> userRole.getRole().getId().equals(roleId));
    }

    /** 관리자 정보 생성 */
    public void createAdminDetail() {
        this.admin = Admin.builder().user(this).build();
    }

    /** 관리자 정보 수정 */
    public void updateAdminDetail(String department) {
        this.getAdmin().updateInfo(department);
    }

    /** 판매자 정보 생성 */
    public void createSellerDetail() {
        this.seller = Seller.builder().user(this).build();
    }

    /** 판매자 정보 수정 */
    public void updateSellerDetail(String companyName, String businessLicenseNumber) {
        this.getSeller().updateInfo(companyName, businessLicenseNumber);
    }

    /** 소비자 정보 생성 */
    public void createCustomerDetail() {
        this.customer = Customer.builder().user(this).build();
    }

    /** 소비자 정보 수정 */
    public void updateCustomerDetail(Address address) {
        this.getCustomer().updateInfo(address);
    }
}
