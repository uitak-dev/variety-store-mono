package com.demo.variety_store_mono.security.jwt;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final Long accessTokenValidityMillis;
    private final Long refreshTokenValidityMillis;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.accessTokenValidityMillis = jwtProperties.getAccessTokenValidityMillis();
        this.refreshTokenValidityMillis = jwtProperties.getRefreshTokenValidityMillis();
    }

    /**
     * 주어진 subject(예: 사용자 식별자)와 추가 클레임을 기반으로 JWT 토큰을 생성합니다.
     *
     * @param subject          토큰의 주체 (예: username 또는 userId)
     * @param additionalClaims 추가적으로 포함할 클레임 (필요 없으면 빈 맵 사용)
     * @return 생성된 JWT 토큰 문자열
     */
    private String generateToken(String subject, Map<String, Object> additionalClaims, Long validityMillis) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("justicexkr23@gmail.com") // 토큰 발급자 설정.
                .issuedAt(now)
                .expiresAt(now.plus(validityMillis, ChronoUnit.MILLIS))
                .subject(subject)
                .claims(claimsMap -> claimsMap.putAll(additionalClaims))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(jwsHeader, claims);

        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    /**
     * 액세스 토큰 생성 (추가 클레임에 "type":"access" 포함)
     */
    public String generateAccessToken(String subject, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("type", "access");
        return generateToken(subject, claims, this.accessTokenValidityMillis);
    }

    /**
     * 리프레시 토큰 생성 (추가 클레임에 "type":"refresh" 포함)
     */
    public String generateRefreshToken(String subject, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("type", "refresh");
        return generateToken(subject, claims, this.refreshTokenValidityMillis);
    }

    /**
     * 리프레시 토큰 검증: JwtDecoder를 이용해 서명 및 클레임("type") 검증
     * 유효한 경우 Jwt 객체를 반환하며, 유효하지 않으면 예외를 던집니다.
     */
    public Jwt validateRefreshToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String type = jwt.getClaim("type");
            if (!"refresh".equals(type)) {
                throw new JwtException("Invalid token type");
            }
            return jwt;
        } catch (JwtException ex) {
            throw new JwtException("Invalid refresh token", ex);
        }
    }
}
