package com.aurine.cloudx.edge.sync.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @description: Redis失效监听配置
 * Redis 需配置 notify-keyspace-events Ex
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-09
 * @Copyright:
 */
@Configuration
public class RedisListenerConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
