package com.aurine.cloud.code.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-15
 * @Copyright:
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    @Value("${spring.kafka.producer.linger-ms}")
    private int lingerMs;
    @Value("${spring.kafka.producer.buffer-memory}")
    private int bufferMemory;

    @Value("${spring.kafka.consumer.security.enable}")
    private boolean securityEnable;
    @Value("${spring.kafka.consumer.security.protocol}")
    private String protocol;
    @Value("${spring.kafka.consumer.security.sasl-mechanism}")
    private String saslMechanism;
    @Value("${spring.kafka.consumer.security.sasl-jaas-config}")
    private String jaasConfig;


    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);//失败是否重发
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);//每次批量发送的数量
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);//批量发送延迟 N ms执行
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //加密
        if (securityEnable) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol);
            props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
            props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }
        return props;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }
}
