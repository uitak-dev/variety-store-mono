package com.demo.variety_store_mono.security.entity;

import com.demo.variety_store_mono.common.entity.Audit;
import com.demo.variety_store_mono.security.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends Audit {

    @Id
    @GeneratedValue
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 토큰 만료 시각
    @Column(nullable = false)
    private Instant expireDate;

    @Builder
    public RefreshToken(Long id, String token, User user, Instant expireDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expireDate = expireDate;
    }

    public void update(String token, Instant expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }
}
