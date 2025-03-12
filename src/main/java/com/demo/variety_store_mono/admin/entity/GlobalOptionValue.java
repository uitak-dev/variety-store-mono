package com.demo.variety_store_mono.admin.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalOptionValue {

    @Id
    @GeneratedValue
    @Column(name = "global_option_value_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "global_option_id", nullable = false)
    private GlobalOption globalOption; // 시스템에 등록된 옵션

    @Column(nullable = false)
    private String optionValue;   // 옵션 값
}
