package com.demo.variety_store_mono.admin.entity;

import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin{

    @Id
    private Long id; // User와 동일한 ID 사용 (Foreign Key)

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String department;

    @Builder
    public Admin(User user, String department) {
        this.user = user;
        this.department = department;
    }

    public void updateInfo(String department) {
        this.department = department;
    }
}
