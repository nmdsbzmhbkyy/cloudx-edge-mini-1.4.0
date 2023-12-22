package com.aurine.cloudx.edge.sync.biz.constant;

import com.aurine.cloudx.edge.sync.common.enums.CascadeDictEnum;
import com.aurine.cloudx.edge.sync.common.enums.SyncTypeEnum;
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
    public static final String PUBLISH_TOPIC_PREFIX = "%s/edge/bridgeMaster/0000/%s/0/0/%s/%s";
    public static final String SUBSCRIBE_TOPIC_PREFIX = "%s/edge/bridgeSlave/0000/%s/0/0/%s/+";
    /** redis标识 */
    private static final String BASE_KEY_PREFIX = "MASTER_SALVE_";
    /** 来源 */
    public static final String SOURCE = DataSourceEnum.MASTER.name;
    /** 本地ip */
    public static final String LOCALHOST_IP = "cloudxedgesyncmaster";
    /** 级联入云类型 */
    public static final String CASCADE_TYPE = CascadeDictEnum.CASCADE.name;
    /** 同步类型  */
    public static final Integer SYNC_TYPE = SyncTypeEnum.MAIN.code;
    /** 转发IP */
    public static final String TRANSFER_IP = "cloudxedgesyncplatformmaster";
    /** 入云申请status字段名 */
    public static final String REQUEST_STATUS_NAME = "cascadeStatus";


    public static final String CONNECTION_STATUS = "ConnectionStatus_master_slave";

    // ------------不同端代码一致--------------
    /** redis UUID前缀 */
    public static final String UUID_MAP_PREFIX = BASE_KEY_PREFIX + "";
    /** redis subscribeConfig前缀 */
    public static final String UUID_SUBSCRIBE_CONFIG = BASE_KEY_PREFIX + "SubscribeConfig";
    /** redis消息队列前缀 */
    public static final String QUEUE_KEY_PREFIX = BASE_KEY_PREFIX + "Queue_";
    /** redis事件消息队列前缀 */
    public static final String EVENT_QUEUE_KEY_PREFIX = BASE_KEY_PREFIX + "Queue_Event_";
    public static final String GRAYSCALE_ROUTING_VERSION = "";

    //------------初始化赋值变量--------------

    public static String localHostPort = "";
    public static String appId = "";
    public static String topicVersion = "1.0";
    public static String edgeSyncTransferUrl = "";
    public static String imgUriPrefix = "";
}
