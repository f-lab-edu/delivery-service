package me.naming.delieveryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource("classpath:application.properties")
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);      // Redis 연결 생성
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();        // RedisTemplate 생성(String, String 형으로 데이터 타입 지정)
        redisTemplate.setConnectionFactory(redisConnectionFactory());               // ConnectionFactory 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());                // Serializer 설정하지 않을 경우 Redis에 저장된 정보가 이상하다...?
        redisTemplate.setValueSerializer(new StringRedisSerializer());              // 위의 설명과 동일하다.
        return redisTemplate;
    }

}

