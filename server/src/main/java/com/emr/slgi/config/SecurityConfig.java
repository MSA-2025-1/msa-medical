package com.emr.slgi.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.emr.slgi.auth.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
        "/auth/**",
        "/error",
        "/api-docs",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/ws/**"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/attachment/**").permitAll()
                .anyRequest().authenticated()
            )
            
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedEntryPoint())
                .accessDeniedHandler(forbiddenHandler())
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authenticationException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            Map<String, Object> body = Map.of(
                "error", "Unauthorized",
                "message", authenticationException.getMessage()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        };
    }

    private AccessDeniedHandler forbiddenHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            Map<String, Object> body = Map.of(
                "error", "Forbidden",
                "message", accessDeniedException.getMessage()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        };
    }
}
