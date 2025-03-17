package com.demo.variety_store_mono.admin.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "optionValue")
public class GlobalOptionValue {

    @Id
    @GeneratedValue
    @Column(name = "global_option_value_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String optionValue;   // 옵션 값

    public GlobalOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
