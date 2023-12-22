package com.aurine.cloudx.estate.thirdparty.transport.mq.kafka;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 项目端生产者
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-10
 * @Copyright:
 */
@Component
public class KafkaProducer implements ApplicationContextAware {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public static KafkaProducer kafkaProducer;

    /**
     * 将对象序列化后，以JSON格式发送给指定主题
     *
     * @param topic
     * @param message
     * @param <T>
     */
    public static <T> void sendMessage(String topic, T message) {
        kafkaProducer.kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }

    /**
     * 将对象序列化后，以JSON格式发送给指定主题
     *
     * @param topic
     * @param key
     * @param message
     * @param <T>
     */
    public static <T> void sendMessage(String topic, String key, T message) {
        kafkaProducer.kafkaTemplate.send(topic, key, JSONObject.toJSONString(message));
    }

    /**
     * 注入spring单例给静态变量
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KafkaProducer.kafkaProducer = applicationContext.getBean(KafkaProducer.class);
    }
}
