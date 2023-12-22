package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

/**
 * @description:各厂家回调接口地址
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
public class CallBackURIConstant {
    /**
     * 停车场 - 赛菲姆
     */
    public static final String PARKING_SFIRM = "/api/callback/parking/sfirm";

    /**
     * 设备中台 - 华为
     */
    public static final String DEVICE_MIDDLE_HUAWEI = "/api/callback/device/huawei";

    /**
     * 设备中台 - 华为
     */
    public static final String DEVICE_MIDDLE_AURINE_EDGE = "/api/callback/device/aurine-edge";
    /**
     * 设备中台 - 冠林
     */
    public static final String DEVICE_MIDDLE_AURINE = "/api/callback/device/aurine";

    /**
     * 设备中台 - 华为 - 临时赋权访问接口
     */
    public static final String DEVICE_MIDDLE_HUAWEI_TEMP_AUTH = "/api/callback/device/huawei/temp";
    /**
     * 边缘网关 - 阿里
     */
    public static final String EDGE_ALI = "/api/callback/edge/ali";
}
