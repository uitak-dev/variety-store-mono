package com.demo.variety_store_mono.admin.entity;


import com.demo.variety_store_mono.common.entity.Audit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalOptionValue extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "global_option_value_id")
    private Long id;

    @Column(nullable = false)
    private String optionValue;   // 옵션 값

    public GlobalOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
