package com.emr.slgi.config;

public class SecurityConstants {

    private SecurityConstants() {}

    public static final String[] PUBLIC_URLS = {
        "/auth/**",
        "/error",
        "/api-docs",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/ws/**",
        "/image/**"
    };

}
