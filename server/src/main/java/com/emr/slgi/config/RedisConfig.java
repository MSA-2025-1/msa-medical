package com.emr.slgi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {

        RedisTemplate<String, String> rt = new RedisTemplate<>();
        rt.setConnectionFactory(cf);

        return rt;

    }

}
