package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用於控制kafka启动与关闭的工具类
 * </p>
 *
 * @ClassName: KafkaUtil
 * @author: wangwei<wangwei @ aurine.cn>
 * @date: 2020年3月30日
 * @Copyright:
 */
@Component
public class AurineKafkaUtil implements ApplicationContextAware {
    private static AurineKafkaUtil kafkaUtil;
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

//    @Value("${spring.kafka.consumer.client-id}")
    private String clientId = AurineConfig.kafkaCliendId;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        kafkaUtil = applicationContext.getBean(AurineKafkaUtil.class);
        kafkaUtil.kafkaListenerEndpointRegistry = applicationContext.getBean(KafkaListenerEndpointRegistry.class);
    }

    /**
     * <p>
     * 启动
     * </p>
     */
    public static void start() {

        MessageListenerContainer listenerContainer = kafkaUtil.kafkaListenerEndpointRegistry.getListenerContainer(kafkaUtil.clientId);
        listenerContainer.start();
//		listenerContainer.resume();
    }

    /**
     * <p>
     * 停止
     * </p>
     */
    public static void stop() {
        MessageListenerContainer listenerContainer = kafkaUtil.kafkaListenerEndpointRegistry.getListenerContainer(kafkaUtil.clientId);
        listenerContainer.stop();
//		listenerContainer.pause();
    }

    /**
     * <p>
     * 暂停
     * </p>
     */
    public static void pause() {
        MessageListenerContainer listenerContainer = kafkaUtil.kafkaListenerEndpointRegistry.getListenerContainer(kafkaUtil.clientId);
        listenerContainer.pause();
    }

    /**
     * <p>
     * 恢复
     * </p>
     */
    public static void resume() {
        MessageListenerContainer listenerContainer = kafkaUtil.kafkaListenerEndpointRegistry.getListenerContainer(kafkaUtil.clientId);
        listenerContainer.resume();
    }

}
