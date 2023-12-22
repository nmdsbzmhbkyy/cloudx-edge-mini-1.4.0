package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-16
 * @Copyright:
 */
@AllArgsConstructor
public enum HuaweiEventTypeMapEnum {
    /**
     * 2  梯口终端 告警事件
     */
    LADDER_WAY_ALERT_THREE_WRONG_PASSWORD(null, HuaweiEventEnum.ALERT_THREE_WRONG_PASSWORD, EventTypeEnum.A002001),
    LADDER_WAY_ALERT_HOLD_TOO_LONG(null, HuaweiEventEnum.ALERT_HOLD_TOO_LONG, EventTypeEnum.A002002),
    LADDER_WAY_ALERT_THREE_WRONG_CARD(null, HuaweiEventEnum.ALERT_THREE_WRONG_CARD, EventTypeEnum.A002003),
    LADDER_WAY_ALERT_OPENDOOR(null, HuaweiEventEnum.ALERT_OPENDOOR, EventTypeEnum.A002004),
    LADDER_WAY_ALERT_OPENDOOR_FORCE(null, HuaweiEventEnum.ALERT_OPENDOOR_FORCE, EventTypeEnum.A002005),
    LADDER_WAY_ALERT_NO_CLOSE(null, HuaweiEventEnum.ALERT_NO_CLOSE, EventTypeEnum.A002006),
    LADDER_WAY_ALERT_BREAK(null, HuaweiEventEnum.ALERT_BREAK, EventTypeEnum.A002007),
    LADDER_WAY_ALERT_BLACKLIST_ENTRY(null, HuaweiEventEnum.ALERT_BLACKLIST_ENTRY, EventTypeEnum.A002008),
    LADDER_WAY_ALERT_FITH_ERROR_OPATER(null, HuaweiEventEnum.ALERT_FITH_ERROR_OPATER, EventTypeEnum.A002009),
    LADDER_WAY_ALERT_DOOR_ERROR(null, HuaweiEventEnum.ALERT_DOOR_ERROR, EventTypeEnum.A002010),
    LADDER_WAY_ALERT_OTHER_ERROR(null, HuaweiEventEnum.ALERT_OTHER_ERROR, EventTypeEnum.A002011),
    /**
     * 3  区口终端 告警事件
     */
    GATE_ALERT_THREE_WRONG_PASSWORD(null, HuaweiEventEnum.ALERT_THREE_WRONG_PASSWORD, EventTypeEnum.A003001),
    GATE_ALERT_HOLD_TOO_LONG(null, HuaweiEventEnum.ALERT_HOLD_TOO_LONG, EventTypeEnum.A003002),
    GATE_ALERT_THREE_WRONG_CARD(null, HuaweiEventEnum.ALERT_THREE_WRONG_CARD, EventTypeEnum.A003003),
    GATE_ALERT_OPENDOOR(null, HuaweiEventEnum.ALERT_OPENDOOR, EventTypeEnum.A003004),
    GATE_ALERT_OPENDOOR_FORCE(null, HuaweiEventEnum.ALERT_OPENDOOR_FORCE, EventTypeEnum.A003005),
    GATE_ALERT_NO_CLOSE(null, HuaweiEventEnum.ALERT_NO_CLOSE, EventTypeEnum.A003006),
    GATE_ALERT_BREAK(null, HuaweiEventEnum.ALERT_BREAK, EventTypeEnum.A003007),
    GATE_ALERT_BLACKLIST_ENTRY(null, HuaweiEventEnum.ALERT_BLACKLIST_ENTRY, EventTypeEnum.A003008),
    GATE_ALERT_FITH_ERROR_OPATER(null, HuaweiEventEnum.ALERT_FITH_ERROR_OPATER, EventTypeEnum.A003009),
    GATE_ALERT_DOOR_ERROR(null, HuaweiEventEnum.ALERT_DOOR_ERROR, EventTypeEnum.A003010),
    GATE_ALERT_OTHER_ERROR(null, HuaweiEventEnum.ALERT_OTHER_ERROR, EventTypeEnum.A003011),


//    ALERT_HOLD_TOO_LONG(null,HuaweiEventEnum.ALERT_HOLD_TOO_LONG, EventTypeEnum.A002002),

    /**
     * 8   智能水表
     */
    waterMeterSupermagnetismAlarm(HuaweiAlarmEventEnum.waterMeterSupermagnetismAlarm, null, EventTypeEnum.A008002),
    waterMeterOverCurrentAlarm(HuaweiAlarmEventEnum.waterMeterOverCurrentAlarm, null, EventTypeEnum.A008003),
    waterMeterLeakCurrentAlarm(HuaweiAlarmEventEnum.waterMeterLeakCurrentAlarm, null, EventTypeEnum.A008004),
    waterMeterOpenLidAlarm(HuaweiAlarmEventEnum.waterMeterOpenLidAlarm, null, EventTypeEnum.A008005),
    waterMeterFault(HuaweiAlarmEventEnum.waterMeterFault, null, EventTypeEnum.A008001),

    /**
     * 9   液位计
     */
    levelGaugeSupermagnetismAlarm(HuaweiAlarmEventEnum.levelGaugeSupermagnetismAlarm, null, EventTypeEnum.A009002),
    levelGaugeLowerLimitAlarm(HuaweiAlarmEventEnum.levelGaugeLowerLimitAlarm, null, EventTypeEnum.A009003),
    levelGaugeOutThresholdAlarm(HuaweiAlarmEventEnum.levelGaugeOutThresholdAlarm, null, EventTypeEnum.A009004),
    levelGaugeLowPowerErrorAlarm(HuaweiAlarmEventEnum.levelGaugeLowPowerErrorAlarm, null, EventTypeEnum.A009005),
    //    waterMeterFault(HuaweiAlarmEventEnum.waterMeterFault,null,EventTypeEnum.A009006),
    levelGaugeFault(HuaweiAlarmEventEnum.levelGaugeFault, null, EventTypeEnum.A009001),

    /**
     * 10  烟感
     */
    smokeAlarm(HuaweiAlarmEventEnum.smokeAlarm, null, EventTypeEnum.A010001),
    smokeLowBatteryAlarm(HuaweiAlarmEventEnum.smokeLowBatteryAlarm, null, EventTypeEnum.A010002),
    /**
     * 11 智能井盖
     */
    WellSupermagnetismAlarm(HuaweiAlarmEventEnum.WellSupermagnetismAlarm, null, EventTypeEnum.A011002),
    WellOpenLidAlarm(HuaweiAlarmEventEnum.WellOpenLidAlarm, null, EventTypeEnum.A011003),
    WellImpactForceAlarm(HuaweiAlarmEventEnum.WellImpactForceAlarm, null, EventTypeEnum.A011004),
    WellWaterLevelAlarm(HuaweiAlarmEventEnum.WellWaterLevelAlarm, null, EventTypeEnum.A011005),
    WellDestoryAlarm(HuaweiAlarmEventEnum.WellDestoryAlarm, null, EventTypeEnum.A011006),
    WellFault(HuaweiAlarmEventEnum.WellFault, null, EventTypeEnum.A011001),


    /**
     * 12  智能路灯
     */
    streetLampDeviceOfflineAlarm(HuaweiAlarmEventEnum.streetLampDeviceOfflineAlarm, null, EventTypeEnum.A012001),
    streetLampOpenLightAlarm(HuaweiAlarmEventEnum.streetLampOpenLightAlarm, null, EventTypeEnum.A012002),
    streetLamplightOffAlarm(HuaweiAlarmEventEnum.streetLamplightOffAlarm, null, EventTypeEnum.A012003),
    streetLampOverpressureAlarm(HuaweiAlarmEventEnum.streetLampOverpressureAlarm, null, EventTypeEnum.A012004),
    streetLampOverCurrentAlarm(HuaweiAlarmEventEnum.streetLampOverCurrentAlarm, null, EventTypeEnum.A012005),
    streetLampUndervoltageAlarm(HuaweiAlarmEventEnum.streetLampUndervoltageAlarm, null, EventTypeEnum.A012006),
    streetLampLightBulbOrCircuitAlarm(HuaweiAlarmEventEnum.streetLampLightBulbOrCircuitAlarm, null, EventTypeEnum.A012007),
    streetLampContactorOrLoopAlarm(HuaweiAlarmEventEnum.streetLampContactorOrLoopAlarm, null, EventTypeEnum.A012008),
    streetLampPowerDriverAlarm(HuaweiAlarmEventEnum.streetLampPowerDriverAlarm, null, EventTypeEnum.A012009);

//    OTHER(null,null, EventTypeEnum.OTHER);


    public HuaweiAlarmEventEnum huaweiAlarmEventEnum;
    public HuaweiEventEnum huaweiEventEnum;
    public EventTypeEnum eventTypeEnum;


    /**
     * 通过华为的事件code，获取到
     *
     * @param huaweiEventEnumCode
     * @return
     */
    public static EventTypeEnum getCloudEnum(String huaweiEventEnumCode) {
        HuaweiEventTypeMapEnum[] enums = values();
        for (HuaweiEventTypeMapEnum huaweiEnum : enums) {
            if (huaweiEnum.huaweiEventEnum.code.equals(huaweiEventEnumCode)) {
                return huaweiEnum.eventTypeEnum;
            }
        }
        return EventTypeEnum.OTHER;
    }


    /**
     * 通过报警的字段code，获取到
     *
     * @param alarmCode
     * @return
     */
    public static EventTypeEnum getEventTypeEnumByAlarmCode(String alarmCode) {
        HuaweiEventTypeMapEnum[] enums = values();
        for (HuaweiEventTypeMapEnum huaweiEnum : enums) {
            if (huaweiEnum.huaweiAlarmEventEnum.code.equals(alarmCode)) {
                return huaweiEnum.eventTypeEnum;
            }
        }
        return EventTypeEnum.OTHER;
    }

    /**
     * 通过报警的字段code，获取到
     *
     * @param alarmCode
     * @return
     */
    public static EventTypeEnum getEventTypeEnumByAlarmCode(String alarmCode, String deviceType) {
        HuaweiEventTypeMapEnum[] enums = values();

        for (HuaweiEventTypeMapEnum huaweiEnum : enums) {
            if (huaweiEnum.huaweiAlarmEventEnum != null
                    && huaweiEnum.huaweiAlarmEventEnum.code.equals(alarmCode)
                    && huaweiEnum.huaweiAlarmEventEnum.deviceTypeCode.equals(deviceType)) {
                return huaweiEnum.eventTypeEnum;
            }
        }
        return EventTypeEnum.OTHER;
    }

    /**
     * 通过华为的事件code，获取到
     *
     * @param huaweiEventEnumCode
     * @return
     */
    public static EventTypeEnum getCloudEnum(String huaweiEventEnumCode, DeviceTypeEnum deviceTypeEnum) {
        HuaweiEventTypeMapEnum[] enums = values();
        for (HuaweiEventTypeMapEnum huaweiEnum : enums) {
            if (huaweiEnum.huaweiEventEnum != null
                    && huaweiEnum.huaweiEventEnum.code.equals(huaweiEventEnumCode)
                    && deviceTypeEnum == huaweiEnum.eventTypeEnum.deviceTypeEnum) {
                return huaweiEnum.eventTypeEnum;
            }
        }
        return EventTypeEnum.OTHER;
    }
}
