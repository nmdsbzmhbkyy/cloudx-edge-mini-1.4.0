package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

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
public enum HuaweiServiceEnum {

    CERT_CARD("CardManager", "卡管理服务","3"),
    CERT_PASSWORD("PasswordManager", "密码管理服务","4"),
    CERT_FACE("FaceManager", "人脸管理服务","2"),
    DEVICE_INFO("DeviceInfo", "设备信息服务",""),
    DEVICE_PARAM("DeviceParam", "设备参数服务",""),
    DEVICE_STATE("DeviceStateChange", "设备状态服务",""),
    ALARM_STATE("AlarmStateChange", "报警状态服务",""),
    DEVICE_CONTROL("DeviceControl", "设备控制服务",""),
    EVENT_REPORT("EventReport", "事件上报服务","");

    public String code;
    public String desc;
    public String cloudCode;


    public static HuaweiServiceEnum getByCode(String code) {
        HuaweiServiceEnum[] huaweiEnums = values();
        for (HuaweiServiceEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.code().equals(code)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    public static HuaweiServiceEnum getByCloudCode(String cloudCode) {
        HuaweiServiceEnum[] huaweiEnums = values();
        for (HuaweiServiceEnum huaweitEnum : huaweiEnums) {
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
