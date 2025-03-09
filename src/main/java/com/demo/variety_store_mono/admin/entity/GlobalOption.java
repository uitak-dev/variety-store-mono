package com.demo.variety_store_mono.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalOption {

    @Id
    @GeneratedValue
    @Column(name = "global_option_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 시스템에 등록된 옵션 이름

    @OneToMany(mappedBy = "globalOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryGlobalOption> categoryGlobalOptions = new ArrayList<>();

    @OneToMany(mappedBy = "globalOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GlobalOptionValue> globalOptionValues = new ArrayList<>();
}
