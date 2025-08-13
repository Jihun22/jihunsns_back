package com.jihunsns_back.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
// ⬇️ 추가
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtProvider {

    private final Key key;
    private final String issuer;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.access-exp-min}") long accessExpMin,
            @Value("${app.jwt.refresh-exp-day}") long refreshExpDay
    ){
        // ⬇️ 변경: 안전한 키 생성
        this.key = buildKey(secret);
        this.issuer = issuer;
        this.accessExpMs = accessExpMin * 60_000;
        this.refreshExpMs = refreshExpDay * 24L * 60L * 60L * 1000L ;
    }

    // ⬇️ 추가: base64 지원 + 최소 32바이트 검증
    private static Key buildKey(String secret) {
        byte[] bytes;
        if (secret != null && secret.startsWith("base64:")) {
            bytes = Base64.getDecoder().decode(secret.substring("base64:".length()));
        } else {
            bytes = (secret == null ? new byte[0] : secret.getBytes(StandardCharsets.UTF_8));
        }
        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 256 bits (32 bytes). " +
                            "Use a longer secret or provide a base64 value with 'base64:' prefix."
            );
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(Long userId , String email , String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(userId))
                .addClaims(Map.of("email", email , "role" , role))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(accessExpMs)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshExpMs)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}