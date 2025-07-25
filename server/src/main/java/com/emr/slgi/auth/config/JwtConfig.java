package com.emr.slgi.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emr.slgi.auth.util.JwtUtil;

@Configuration
public class JwtConfig {

    @Bean(name = "accessJwtUtil")
    public JwtUtil accessJwtUtil(@Value("${jwt.access-token-secret}") String accessTokenSecret) {
        return new JwtUtil(accessTokenSecret);
    }

    @Bean(name = "refreshJwtUtil")
    public JwtUtil refreshJwtUtil(@Value("${jwt.refresh-token-secret}") String refreshTokenSecret) {
        return new JwtUtil(refreshTokenSecret);
    }

}
