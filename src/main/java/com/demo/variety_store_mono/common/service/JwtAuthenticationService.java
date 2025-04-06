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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
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
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // 추가 클레임으로 역할 정보 등을 포함할 수 있습니다.
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList(),
                        "id", user.getId())
        );
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(
                user.getUserName(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList(),
                        "id", user.getId())
        );

        // 리프레시 토큰 생성.
        RefreshToken saveRefreshToken;
        RefreshToken.RefreshTokenBuilder refreshTokenBuilder = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expireDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenValidityMillis()));

        if (user.getRefreshToken() == null) {
            saveRefreshToken = refreshTokenBuilder.build();
        } else {
            saveRefreshToken = refreshTokenBuilder.id(user.getRefreshToken().getId()).build();
        }

        refreshTokenRepository.save(saveRefreshToken);

        return new TokenResponse(accessToken, newRefreshToken);
    }

    /** Refresh Token 을 이용하여 새로운 Access Token 발급. */
    public String refreshAccessToken(String refreshToken) {

        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("invalid token id"));

        // JwtTokenProvider를 이용한 토큰 검증
        Jwt jwt = jwtTokenProvider.validateRefreshToken(findRefreshToken.getToken());
        String userName = jwt.getSubject();
        Long userId = jwt.getClaim("id");

        if (findRefreshToken.getExpireDate().isBefore(Instant.now())) {
            // 만료된 토큰은 DB에서 삭제 후 예외 처리
            refreshTokenRepository.delete(findRefreshToken);
            throw new RuntimeException("refresh token expired");
        }

        // 새로운 Access Token 발급.
        return jwtTokenProvider.generateAccessToken(
                userName,
                Map.of("roles", findRefreshToken.getUser().getRoles(),
                        "id", userId)
        );
    }

    /**
     * 로그아웃: 전달받은 리프레시 토큰을 DB에서 삭제하여 무효화.
     */
    public void logout(String refreshToken) {
        // refreshToken 조회
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token id"));

        // 연관관계 끊기: User에서 refreshToken 필드를 null로 설정
        User user = findRefreshToken.getUser();
        if (user != null) {
            user.deleteRefreshToken();
        }

        // refreshToken 삭제
        refreshTokenRepository.delete(findRefreshToken);
    }
}
