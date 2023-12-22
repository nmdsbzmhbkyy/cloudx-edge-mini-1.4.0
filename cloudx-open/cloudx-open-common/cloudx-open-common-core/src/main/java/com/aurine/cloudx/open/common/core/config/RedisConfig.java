package com.aurine.cloudx.open.common.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author : Qiu
 * @date : 2021 01 24 09:21
 */

@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisTemplate openRedisTemplate(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer serializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // redis连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // redis.key序列化器
        template.setKeySerializer(stringRedisSerializer);
        // redis.value序列化器
        template.setValueSerializer(serializer);
        // redis.hash.key序列化器
        template.setHashKeySerializer(stringRedisSerializer);
        // redis.hash.value序列化器
        template.setHashValueSerializer(serializer);
        // 调用其他初始化逻辑
        template.afterPropertiesSet();
        // 这里设置redis事务一致
        template.setEnableTransactionSupport(true);

        return template;
    }

    /**
     * 配置redis Json序列化器
     *
     * @return
     */
    @Bean
    public Jackson2JsonRedisSerializer redisJsonSerializer() {
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 解决jackson2无法反序列化LocalDateTime的问题
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // deprecated
        serializer.setObjectMapper(mapper);
        return serializer;
    }
}
