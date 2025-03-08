package com.demo.variety_store_mono.common.service;

import com.demo.variety_store_mono.security.entity.RefreshToken;
import com.demo.variety_store_mono.admin.entity.Role;
import com.demo.variety_store_mono.security.entity.User;
import com.demo.variety_store_mono.security.repository.RefreshTokenRepository;
import com.demo.variety_store_mono.security.repository.UserRepository;
import com.demo.variety_store_mono.common.request.LoginRequest;
import com.demo.variety_store_mono.common.response.TokenResponse;
import com.demo.variety_store_mono.security.jwt.JwtProperties;
import com.demo.variety_store_mono.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    /**
     * 로그인: 사용자 인증 후 액세스 토큰과 리프레시 토큰을 발급하고, 리프레시 토큰을 DB에 저장.
     */
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 추가 클레임으로 역할 정보 등을 포함할 수 있습니다.
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList())
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList())
        );

        // DB에 리프레시 토큰 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenValidityMillis()))
                .build();

        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);

        return new TokenResponse(accessToken, refreshTokenEntity.getId(), refreshToken);
    }

    /** Refresh Token 을 이용하여 새로운 Access Token 발급. */
    public String refreshAccessToken(Long refreshTokenId) {

        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new RuntimeException("invalid token id"));

        // JwtTokenProvider를 이용한 토큰 검증
        Jwt jwt = jwtTokenProvider.validateRefreshToken(refreshToken.getToken());
        String userName = jwt.getSubject();

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            // 만료된 토큰은 DB에서 삭제 후 예외 처리
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("refresh token expired");
        }

        // 새로운 Access Token 발급.
        return jwtTokenProvider.generateAccessToken(userName, Map.of("roles", refreshToken.getUser().getRoles()));
    }

    /**
     * 로그아웃: 전달받은 리프레시 토큰을 DB에서 삭제하여 무효화.
     */
    public void logout(Long refreshTokenId) {
        // refreshToken 조회
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token id"));

        // 연관관계 끊기: User에서 refreshToken 필드를 null로 설정
        User user = refreshToken.getUser();
        if (user != null) {
            user.deleteRefreshToken();
        }

        // refreshToken 삭제
        refreshTokenRepository.delete(refreshToken);
//        refreshTokenRepository.flush();
    }
}
