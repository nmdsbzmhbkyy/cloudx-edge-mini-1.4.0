package com.aurine.cloudx.edge.sync.biz.constant;

import com.aurine.cloudx.edge.sync.common.enums.CascadeDictEnum;
import com.aurine.cloudx.edge.sync.common.enums.DataSourceEnum;

/**
 * @Author: wrm
 * @Date: 2021/12/24 15:34
 * @Package: com.aurine.cloudx.edge.sync.biz
 * @Version: 1.0
 * @Remarks:
 **/
public class Constants {
    // ------------不同端代码不一致--------------
    /** topic主题 */
    public static final String PUBLISH_TOPIC_PREFIX = "%s/edge/cloudSlave/0000/%s/0/0/%s/%s";
    public static final String SUBSCRIBE_TOPIC_PREFIX = "%s/edge/platform/0000/%s/0/0/%s/+";
    /** redis标识 */
    private static final String BASE_KEY_PREFIX = "PLATFORM_MASTER_";
    /** 来源 */
    public static final String SOURCE = DataSourceEnum.MASTER.name;
    /** 本地ip */
    public static final String LOCALHOST_IP = "cloudxedgesyncplatformmaster";
    /** 级联入云类型 */
    public static final String CASCADE_TYPE = CascadeDictEnum.CLOUD.name;
    /** 转发IP */
    public static final String TRANSFER_IP = "cloudxedgesyncmaster";
    /** 入云申请status字段名 */
    public static final String REQUEST_STATUS_NAME = "cloudStatus";

    public static final String CONNECTION_STATUS = "ConnectionStatus_plarform_master";

    // ------------不同端代码一致--------------
    /** redis UUID前缀 */
    public static final String UUID_MAP_PREFIX = BASE_KEY_PREFIX + "";
    /** redis subscribeConfig前缀 */
    public static final String UUID_SUBSCRIBE_CONFIG = BASE_KEY_PREFIX + "SubscribeConfig";
    /** redis 正在发送消息taskId列表 */
    public static final String SENDING_TASKID_LIST_PREFIX = BASE_KEY_PREFIX + "Sending_TaskId_List_";
    /** redis消息发送次数前缀 */
    public static final String RETRY_COUNT_KEY_PREFIX = BASE_KEY_PREFIX + "Retry_Count_";
    /** redis消息队列前缀 */
    public static final String QUEUE_KEY_PREFIX = BASE_KEY_PREFIX + "Queue_";
    /** redis事件消息队列前缀 */
    public static final String EVENT_QUEUE_KEY_PREFIX = BASE_KEY_PREFIX + "Queue_Event_";

    public static final String GRAYSCALE_ROUTING_VERSION = "";

    public static final String SYNC_DISPATCH_TASK_INFO_LOCK_PREFIX = "SYNC_DISPATCH_TASK_INFO_LOCK_";

    public static final String SYNC_DISPATCH_TASK_INFO_JOB_LOCK = "SYNC_DISPATCH_TASK_INFO_JOB_LOCK";

    public static final String DISPATCH_PROJECT_UUID = "DISPATCH_PROJECT_UUID";
    public static final String EVENT_DISPATCH_PROJECT_UUID = "EVENT_DISPATCH_PROJECT_UUID";
    public static final String SEND_PROJECT_UUID = "SNED_PROJECT_UUID";
    public static final String EVENT_SNED_PROJECT_UUID = "EVENT_SNED_PROJECT_UUID";

    //------------初始化赋值变量--------------

    public static String localHostPort = "";
    public static String appId = "";
    public static String topicVersion = "1.0";
    public static String edgeSyncTransferUrl = "";
    public static String imgUriPrefix = "";
}
