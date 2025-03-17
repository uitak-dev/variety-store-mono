package com.demo.variety_store_mono.seller.entity;

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
public class Seller {

    @Id
    private Long id; // User와 동일한 ID 사용 (Foreign Key)

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String businessLicenseNumber;

    @OneToMany(mappedBy = "seller")
    private Set<Product> products = new LinkedHashSet<>();

    @Builder
    public Seller(User user, String companyName, String businessLicenseNumber) {
        this.user = user;
        this.companyName = companyName;
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public void updateInfo(String companyName, String businessLicenseNumber) {
        this.companyName = companyName;
        this.businessLicenseNumber = businessLicenseNumber;
    }
}
