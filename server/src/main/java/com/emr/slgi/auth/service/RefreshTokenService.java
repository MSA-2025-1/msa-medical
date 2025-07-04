package com.emr.slgi.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.emr.slgi.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    // TODO: appendonly 설정 알아보기

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    public String createRefreshToken(String memberUuid) {
        Map<String, String> map = Map.of(
            "uuid", memberUuid,
            "jti", UUID.randomUUID().toString()
        );
        Date twoWeeksLater = Date.from(Instant.now().plus(14, ChronoUnit.DAYS));

        return jwtUtil.generateToken(map, twoWeeksLater, refreshTokenSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return jwtUtil.parseToken(refreshToken, refreshTokenSecret);
    }

    public void blacklistTokenJti(String jti, Date exp) {
        String key = "blacklist:jti:" + jti;
        Duration remaining = Duration.between(Instant.now(), exp.toInstant());
        remaining = remaining.isNegative() ? Duration.ZERO : remaining;
        stringRedisTemplate.opsForValue().set(key, "1", remaining);
    }

    public boolean isTokenBlacklisted(String jti) {
        String key = "blacklist:jti:" + jti;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

}
