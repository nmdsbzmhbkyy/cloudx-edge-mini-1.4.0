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
    String TOPIC_CONFIG = "OPENAPI-PLATFORM_CONFIG_OUT";

    /**
     * open通用需关注顺序消息订阅
     *
     * 接收推送需要关注顺序的通用消息，需要在一个partition有序执行
     * 包括，级联入云消息，操作类消息，命令类消息，其他类消息
     */
    String TOPIC_COMMON = "OPENAPI-PLATFORM_COMMON_OUT";

    /**
     * open事件类消息订阅
     *
     * 接收推送事件类消息
     */
    String TOPIC_EVENT = "OPENAPI-PLATFORM_EVENT_OUT";

    /**
     * 设备端推送，设备管理业务使用，from zouyu ，从constants移动到此类
     */
    String EDGE_INTERCOM_TOPIC = "EDGE_INTERCOM_TOPIC";
}
