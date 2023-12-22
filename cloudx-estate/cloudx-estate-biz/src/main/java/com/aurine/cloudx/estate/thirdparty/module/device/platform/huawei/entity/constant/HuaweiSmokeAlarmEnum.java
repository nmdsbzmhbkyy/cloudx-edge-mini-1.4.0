package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 烟感错误类型code对应   物联网综合报警类型HuaweiAlarmEventEnum   code
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-09 11:31
 */
@AllArgsConstructor
public enum HuaweiSmokeAlarmEnum {

    smokeAlarm("0", "smokeAlarm"),
    smokeLowBatteryAlarm("1", "smokeLowBatteryAlarm");

    public String code;
    public String desc;

    public static String getDescByCode(String code) {
        HuaweiSmokeAlarmEnum[] huaweiSmokeAlarmEnums = values();
        for (HuaweiSmokeAlarmEnum huaweiSmokeAlarmEnum : huaweiSmokeAlarmEnums) {
            if (huaweiSmokeAlarmEnum.code().equals(code)) {
                return huaweiSmokeAlarmEnum.desc;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
