package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 路灯错误类型code对应   物联网综合报警类型HuaweiAlarmEventEnum   code
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-09 11:23
 */
@AllArgsConstructor
public enum HuaweiStreetLampAlarmEnum {

    streetLampDeviceOfflineAlarm("0", "streetLampDeviceOfflineAlarm"),
    streetLampOpenLightAlarm("1", "streetLampOpenLightAlarm"),
    streetLamplightOffAlarm("2", "streetLamplightOffAlarm"),
    streetLampOverpressureAlarm("3", "streetLampOverpressureAlarm"),
    streetLampOverCurrentAlarm("4", "streetLampOverCurrentAlarm"),
    streetLampUndervoltageAlarm("5", "streetLampUndervoltageAlarm"),
    streetLampLightBulbOrCircuitAlarm("6", "streetLampLightBulbOrCircuitAlarm"),
    streetLampContactorOrLoopAlarm("7", "streetLampContactorOrLoopAlarm"),
    streetLampPowerDriverAlarm("8", "streetLampPowerDriverAlarm");

    public String code;
    public String desc;

    public static String getDescByCode(String code) {
        HuaweiStreetLampAlarmEnum[] huaweiStreetLampAlarmEnums = values();
        for (HuaweiStreetLampAlarmEnum huaweiStreetLampAlarmEnum : huaweiStreetLampAlarmEnums) {
            if (huaweiStreetLampAlarmEnum.code().equals(code)) {
                return huaweiStreetLampAlarmEnum.desc;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
