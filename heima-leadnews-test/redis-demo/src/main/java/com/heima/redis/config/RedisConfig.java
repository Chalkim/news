package com.heima.redis.config;

import com.heima.redis.dtos.LikesBehaviorDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // @Bean
    // public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    //     RedisTemplate<String, Object> template = new RedisTemplate<>();
    //     template.setConnectionFactory(factory);

    //     // 设置key和value的序列化器
    //     StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    //     template.setKeySerializer(stringRedisSerializer);
    //     template.setValueSerializer(stringRedisSerializer);
    //     template.setHashKeySerializer(stringRedisSerializer);
    //     template.setHashValueSerializer(stringRedisSerializer);

    //     return template;
    // }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置key的序列化器
        template.setKeySerializer(stringRedisSerializer);
        // 设置value的序列化器为protobuf序列化器
        template.setValueSerializer(new ProtoBufRedisSerializer());
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(new ProtoBufRedisSerializer());

        return template;
    }
}
