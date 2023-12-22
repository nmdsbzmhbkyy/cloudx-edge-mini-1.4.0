package com.aurine.cloudx.open.origin.config.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-15
 * @Copyright:
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;
    @Value("${spring.kafka.consumer.session-timeout}")
    private String sessionTimeout;
    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.concurrency}")
    private int concurrency;

    @Value("${spring.kafka.consumer.max-poll-records:500}")
    private int maxPollRecords;//单次拉去的数量,默认500

    @Value("${spring.kafka.consumer.max-poll-interval-ms:3000000}")
    private int maxPollIntervalMs;//单次拉去处理的超时事件，默认3000000ms 300秒

     @Value("${spring.kafka.consumer.client-id:consumer}")
    private String clientId;


    @Value("${spring.kafka.consumer.security.enable}")
    private boolean securityEnable;
    @Value("${spring.kafka.consumer.security.protocol}")
    private String protocol;
    @Value("${spring.kafka.consumer.security.sasl-mechanism}")
    private String saslMechanism;
    @Value("${spring.kafka.consumer.security.sasl-jaas-config}")
    private String jaasConfig;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);

//        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

        //加密
        if (securityEnable) {
            propsMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol);
            propsMap.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
            propsMap.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }

        return propsMap;
    }
//    /**
//     * kafka监听
//     * @return
//     */
//    @Bean
//    public RawDataListener listener() {
//        return new RawDataListener();
//    }
}
