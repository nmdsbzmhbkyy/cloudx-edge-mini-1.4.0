package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

/**
 * 主题Topic常量
 *
 * @ClassName: TopicConstant
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 13:56
 * @Copyright:
 */
public class TopicConstant {

    /**
     * 响应 - 数据查询
     */
    public static final String SDI_RESPONSE_SEARCH_RECORD = "API_RESPONSE_SEARCH_RECORD";

    /**
     * 响应 - 操作响应
     */
    public static final String SDI_RESPONSE_OPERATE = "API_RESPONSE_OPERATE";

    /**
     * 事件 - 设备事件 - 门禁通行
     */
    public static final String SDI_EVENT_DEVICE_GATE_PASS = "API_EVENT_DEVICE_GATE_PASS";


    /**
     * 事件 - 设备事件 - 车场通行
     */
    public static final String SDI_EVENT_DEVICE_PARKING_PASS = "API_EVENT_DEVICE_PARKING_PASS";
    /**
     * 事件 - 设备事件 - 车场缴费
     */
    public static final String SDI_EVENT_DEVICE_PARKING_PAY = "API_EVENT_DEVICE_PARKING_PAY";
    /**
     * 事件 - 设备事件 - 通知
     */
    public static final String SDI_EVENT_DEVICE_NOTICE = "API_EVENT_DEVICE_NOTICE";

    /**
     * 事件 - 告警 - 阈值告警
     */
    public static final String SDI_EVENT_WARNING_THRESHOLD = "API_EVENT_WARNING_THRESHOLD";

    /**
     * 事件 - 告警 - 异常
     */
    public static final String SDI_EVENT_WARNING_ERROR = "API_EVENT_WARNING_ERROR";

    /**
     * IOT设备事件回调
     */
    public static final String IOT_DEVICE_EVENT = "IOT_DEVICE_EVENT";

    /**
     * 车场在线状态
     */
    public static final String SDI_EVENT_PARKING_STATUS= "API_EVENT_PARKING_STATUS";


    /**
     * 边缘侧发送云端topic
     */
    public static final String EDGE_SYNC_EDGE_PLATFORM = "EDGE_SYNC_EDGE_PLATFORM";
    /**
     * 人行实时记录
     */
    public static final String PEOPLE_PASS_RECORD = "PEOPLE_PASS_RECORD";
    /**
     * 报警事件
     */
    public static final String ALARM_EVENT = "ALARM_EVENT";
    /**
     * 设备状态变更
     */
    public static final String DEVICE_STATUS = "DEVICE_STATUS";

    /**
     * 人脸抓拍记录
     */
    public static final String PEOPLE_FACE_RECORD = "PEOPLE_FACE_RECORD";

    /**
     * 设备管理（增删改）
     */
    public static final String OPEN_V2_DEVICE_MANGE="OPEN_V2_DEVICE_MANGE";
}
