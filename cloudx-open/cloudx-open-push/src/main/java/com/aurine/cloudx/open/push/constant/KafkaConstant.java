package com.aurine.cloudx.open.push.constant;

/**
 * Kafka常量
 *
 * @author : Qiu
 * @date : 2022/7/25 14:19
 */
public interface KafkaConstant {

    /**
     * 入口主题-推送配置类消息
     */
    String TOPIC_PUSH_CONFIG = "OPENAPI-PUSH_CONFIG_IN";

    /**
     * 入口主题-推送级联入云类消息
     */
    String TOPIC_PUSH_CASCADE = "OPENAPI-PUSH_CASCADE_IN";

    /**
     * 入口主题-推送操作类消息
     */
    String TOPIC_PUSH_OPERATE = "OPENAPI-PUSH_OPERATE_IN";

    /**
     * 入口主题-推送指令类消息
     */
    String TOPIC_PUSH_COMMAND = "OPENAPI-PUSH_COMMAND_IN";

    /**
     * 入口主题-推送事件类消息
     */
    String TOPIC_PUSH_EVENT = "OPENAPI-PUSH_EVENT_IN";

    /**
     * 入口主题-推送其他消息
     */
    String TOPIC_PUSH_OTHER = "OPENAPI-PUSH_OTHER_IN";
}
