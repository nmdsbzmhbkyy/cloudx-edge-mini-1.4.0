package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 物联网综合报警类型
 *
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-09 11:50
 */
@AllArgsConstructor
public enum HuaweiAlarmEventEnum {

    /**
     * 智能水表   8
     */
    waterMeterSupermagnetismAlarm("supermagnetismAlarm", "超磁报警", "8"),
    waterMeterOverCurrentAlarm("overCurrentAlarm", "过流报警", "8"),
    waterMeterLeakCurrentAlarm("leakCurrentAlarm", "漏流报警", "8"),
    waterMeterOpenLidAlarm("openLidAlarm", "开盖报警", "8"),
    waterMeterFault("fault", "故障", "8"),

    /**
     * 液位计    9
     */
    levelGaugeSupermagnetismAlarm("supermagnetismAlarm", "超磁报警", "9"),
    levelGaugeLowerLimitAlarm("lowerLimitAlarm", "液位低下限报警", "9"),
    levelGaugeOutThresholdAlarm("outThresholdAlarm", "液位超阈值报警", "9"),
    levelGaugeLowPowerErrorAlarm("lowPowerErrorAlarm", "无法进入低功耗报警", "9"),
    levelGaugeFault("fault", "故障", "9"),

    /**
     * 烟感      10
     */
    smokeAlarm("smokeAlarm", "烟雾报警", "10"),
    smokeLowBatteryAlarm("smokeLowBatteryAlarm", "低电报警", "10"),
    /**
     * 智能井盖    11
     */
    WellSupermagnetismAlarm("supermagnetismAlarm", "超磁报警", "11"),
    WellOpenLidAlarm("openLidAlarm", "开盖报警", "11"),
    WellImpactForceAlarm("impactForceAlarm", "强烈撞击报警", "11"),
    WellWaterLevelAlarm("waterLevelAlarm", "水位报警", "11"),
    WellDestoryAlarm("destoryAlarm", "盗窃报警", "11"),
    WellFault("fault", "故障", "11"),


    /**
     * 路灯     12
     */
    streetLampDeviceOfflineAlarm("streetLampDeviceOfflineAlarm", "设备离线", "12"),
    streetLampOpenLightAlarm("streetLampOpenLightAlarm", "异常开灯", "12"),
    streetLamplightOffAlarm("streetLamplightOffAlarm", "异常关灯", "12"),
    streetLampOverpressureAlarm("streetLampOverpressureAlarm", "设备过压", "12"),
    streetLampOverCurrentAlarm("streetLampOverCurrentAlarm", "设备过流", "12"),
    streetLampUndervoltageAlarm("streetLampUndervoltageAlarm", "设备欠压", "12"),
    streetLampLightBulbOrCircuitAlarm("streetLampLightBulbOrCircuitAlarm", "灯泡或线路异常", "12"),
    streetLampContactorOrLoopAlarm("streetLampContactorOrLoopAlarm", "接触器或回路异常", "12"),
    streetLampPowerDriverAlarm("streetLampPowerDriverAlarm", "电源驱动器异常", "12");

    public String code;
    public String desc;
    public String deviceTypeCode;

    public static HuaweiAlarmEventEnum getByCode(String code) {
        HuaweiAlarmEventEnum[] huaweiAlarmEventEnums = values();
        for (HuaweiAlarmEventEnum huaweiAlarmEventEnum : huaweiAlarmEventEnums) {
            if (huaweiAlarmEventEnum.code().equals(code)) {
                return huaweiAlarmEventEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
