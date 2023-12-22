package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11
 * @Copyright:
 */
public class AurineConstant {
    public static final String STATE_SUCCESS = "0";

    /**
     * kafka 请求主题
     */
    public static final String KAFKA_TOPIC_REQUEST = "aurine_middle_platform_request";

    /**
     * kafka 回调主题
     */
    public static final String KAFKA_TOPIC_CALLBACK = "aurine_middle_platform_callback";
    /**
     * kafka 主题 分组
     */
    public static final String KAFKA_GROUP = "aurine_middle_platform_group";

    /**
     * kafka 客户端id
     */
    public static final String KAFKA_CLIEND_ID = "aurine01";

    /**
     * 消息ID参数前缀
     */
    public static final String MESSAGE_ID_PRE = "AURINE_MIDDLE_REQUEST_";
    /**
     * NOTICE 请求参数前缀
     */
    public static final String MESSAGE_NOTICE_ID_PRE = "AURINE_MIDDLE_REQUEST_NOTICE_";
    /**
     * 开门 请求参数前缀
     */
    public static final String MESSAGE_OPEN_DOOR_ID_PRE = "AURINE_MIDDLE_REQUEST_OPENDOOR_";
}
