package com.aurine.cloudx.edge.sync.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 邹宇
 * @Date: 2022-3-17 15:29:31
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks: 初始化参数配置
 **/
@Configuration
@Data
@Getter
public class MqttParamConfig {
    /**
     * mqttBrokerIp
     */
    @Value("${mqtt.mqttBrokerIp}")
    private String mqttBrokerIp;

    /**
     * mqttBrokerPort
     */
    @Value("${mqtt.mqttBrokerPort}")
    private String mqttBrokerPort;

    /**
     * userName
     */
    @Value("${mqtt.userName}")
    private String userName;

    /**
     * password
     */
    @Value("${mqtt.password}")
    private String password;

    /**
     * mqttClientId
     */
    @Value("${mqtt.mqttClientId}")
    private String mqttClientId;

    /**
     * 过期时间
     */
    @Value("${mqtt.expiration}")
    private long expiration;

    /**
     * 重试次数
     */
    @Value("${mqtt.retryCount}")
    private Integer retryCount;

}
