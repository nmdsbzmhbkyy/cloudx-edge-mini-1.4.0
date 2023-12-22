package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 华为中台 服务 枚举
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-10-10
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeServiceEnum {

    CERT_CARD("CardManager", "卡管理服务","3"),
    CERT_PASSWORD("PasswordManager", "密码管理服务","4"),
    CERT_FACE("FaceManager", "人脸管理服务","2"),
    DEVICE_INFO("DeviceInfo", "设备信息服务",""),
    DEVICE_PARAM("DeviceParam", "设备参数服务",""),
    DEVICE_PARAMS("DeviceParams", "设备参数服务",""),
    DEVICE_STATE("DeviceStateChange", "设备状态服务",""),
    ALARM_STATE("AlarmStateChange", "报警状态服务",""),
    DEVICE_CONTROL("DeviceControl", "设备控制服务",""),
    INFO_MANAGER("InfoManager", "信息管理服务",""),
    EVENT_REPORT("EventReport", "事件上报服务",""),
    CHANNEl_LIST_QUERY("channelListQuery", "通道列表查询",""),
    CHANNEl_OPERATION("channelOperation","通道设置",""),
    CHANNEl_STATUS_QUERY("channelStatusQuery", "通道状态查询",""),
    CLEAR_ALARM("clearAlarm","消除报警",""),
    PASS("pass","门禁通行","");

    public String code;
    public String desc;
    public String cloudCode;


    public static AurineEdgeServiceEnum getByCode(String code) {
        AurineEdgeServiceEnum[] huaweiEnums = values();
        for (AurineEdgeServiceEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.code().equals(code)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    public static AurineEdgeServiceEnum getByCloudCode(String cloudCode) {
        AurineEdgeServiceEnum[] huaweiEnums = values();
        for (AurineEdgeServiceEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.cloudCode.equals(cloudCode)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
