package com.demo.variety_store_mono.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "global_option_id")  // 외래키를 GlobalOptionValue 테이블에 직접 생성
    private Set<GlobalOptionValue> globalOptionValues = new LinkedHashSet<>();

    @Builder
    public GlobalOption(String name, Set<GlobalOptionValue> globalOptionValues) {
        this.name = name;
        this.globalOptionValues = globalOptionValues != null
                ? new LinkedHashSet<>(globalOptionValues)
                : new LinkedHashSet<>();
    }

    /** * * * * * * * * * * * * * * * *
     * association convenience method *
     * * * * * * * * * * * * * * * * */

    // 옵션 값 목록 수정(옵션 값 추가/제거).
    public void updateGlobalOption(String name, Set<GlobalOptionValue> values) {

        this.name = name;

        // 삭제할 옵션 값
        Set<GlobalOptionValue> valueToRemove = new LinkedHashSet<>(globalOptionValues);
        valueToRemove.removeAll(values);

        // 추가할 옵션 값
        LinkedHashSet<GlobalOptionValue> valueToAdd = new LinkedHashSet<>(values);
        valueToAdd.removeAll(globalOptionValues);

        // 옵션 값 삭제
        Iterator<GlobalOptionValue> iterator = globalOptionValues.iterator();
        while(iterator.hasNext()) {
            GlobalOptionValue value = iterator.next();
            if (valueToRemove.contains(value)) {
                iterator.remove();
            }
        }

        // 옵션 값 추가
        globalOptionValues.addAll(valueToAdd);
    }
}
