package com.aurine.cloudx.edge.sync.biz.constant;

/**
 * @Author: wrm
 * @Date: 2021/12/24 15:34
 * @Package: com.aurine.cloudx.edge.sync.biz
 * @Version: 1.0
 * @Remarks: kafka常量
 **/
public interface KafkaConstant {

    /**
     * open配置类型消息订阅
     */
    String TOPIC_CONFIG = "OPENAPI-PLATFORM-MASTER_CONFIG_OUT";

    /**
     * open通用需关注顺序消息订阅
     *
     * 接收推送需要关注顺序的通用消息，需要在一个partition有序执行
     * 包括，级联入云消息，操作类消息，命令类消息，其他类消息
     */
    String TOPIC_COMMON = "OPENAPI-PLATFORM-MASTER_COMMON_OUT";

    /**
     * open事件类消息订阅
     *
     * 接收推送事件类消息
     */
    String TOPIC_EVENT = "OPENAPI-PLATFORM-MASTER_EVENT_OUT";

    /**
     * 转发通用需关注顺序消息
     */
    String TRANSFER_TOPIC_COMMON = "OPENAPI-MASTER-SLAVE_COMMON_OUT";

    /**
     *
     * 转发事件消息
     */
    String TRANSFER_TOPIC_EVENT = "OPENAPI-MASTER-SLAVE_EVENT_OUT";
}
