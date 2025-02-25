package com.demo.variety_store_mono.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;    // ex) ROLE_ADMIN, ROLE_MANAGER, ROLE_SELLER

    private String description;

    @Builder
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
