package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;


import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-26 15:38
 */
@AllArgsConstructor
public enum  AurineEventTypeMapEnum {

    LADDER_WAY_ALERT_OPENDOOR(AurineAlarmEnum.ALERT_OPENDOOR,EventTypeEnum.A002004),
    LADDER_WAY_ALERT_THREE_WRONG_PASSWORD(AurineAlarmEnum.ALERT_THREE_WRONG_PASSWORD, EventTypeEnum.A002001),
    LADDER_WAY_ALERT_HOLD_TOO_LONG(AurineAlarmEnum.ALERT_HOLD_TOO_LONG, EventTypeEnum.A002002),
    LADDER_WAY_ALERT_THREE_WRONG_CARD(AurineAlarmEnum.ALERT_THREE_WRONG_CARD, EventTypeEnum.A002003),
    //    ALERT_THREE_WRONG_FINGER(AurineAlarmEnum.ALERT_THREE_WRONG_CARD, "3次指纹错误告警"),
    LADDER_WAY_ALERT_OPENDOOR_FORCE(AurineAlarmEnum.ALERT_OPENDOOR_FORCE, EventTypeEnum.A002005),
    LADDER_WAY_ALERT_NO_CLOSE(AurineAlarmEnum.ALERT_NO_CLOSE, EventTypeEnum.A002006),
    LADDER_WAY_ALERT_BREAK(AurineAlarmEnum.ALERT_BREAK, EventTypeEnum.A002007),
    LADDER_WAY_ALERT_OTHER_ERROR(AurineAlarmEnum.ALERT_OTHER_ERROR, EventTypeEnum.A002008),

    GATE_ALERT_OPENDOOR(AurineAlarmEnum.ALERT_OPENDOOR,EventTypeEnum.A003004),
    GATE_ALERT_THREE_WRONG_PASSWORD(AurineAlarmEnum.ALERT_THREE_WRONG_PASSWORD, EventTypeEnum.A003001),
    GATE_ALERT_HOLD_TOO_LONG(AurineAlarmEnum.ALERT_HOLD_TOO_LONG, EventTypeEnum.A003002),
    GATE_ALERT_THREE_WRONG_CARD(AurineAlarmEnum.ALERT_THREE_WRONG_CARD, EventTypeEnum.A003003),
    //    ALERT_THREE_WRONG_FINGER(AurineAlarmEnum.ALERT_THREE_WRONG_CARD, "3次指纹错误告警"),
    GATE_ALERT_OPENDOOR_FORCE(AurineAlarmEnum.ALERT_OPENDOOR_FORCE, EventTypeEnum.A003005),
    GATE_ALERT_NO_CLOSE(AurineAlarmEnum.ALERT_NO_CLOSE, EventTypeEnum.A003006),
    GATE_ALERT_BREAK(AurineAlarmEnum.ALERT_BREAK, EventTypeEnum.A003007),
    GATE_ALERT_OTHER_ERROR(AurineAlarmEnum.ALERT_OTHER_ERROR, EventTypeEnum.A003008),



    OTHER(null,EventTypeEnum.OTHER);

    public AurineAlarmEnum aurineAlarmEnum;
    public EventTypeEnum eventTypeEnum;


    /**
     * 通过华为的事件code，获取到
     *
     * @param aurineEventEnumCode
     * @return
     */
    public static EventTypeEnum getCloudEnum(String aurineEventEnumCode, DeviceTypeEnum deviceTypeEnum) {
        AurineEventTypeMapEnum[] enums = values();
        for (AurineEventTypeMapEnum aurineEvent : enums) {
            if (aurineEvent.aurineAlarmEnum != null
                    && aurineEvent.aurineAlarmEnum.code.equals(aurineEventEnumCode)
                    && deviceTypeEnum == aurineEvent.eventTypeEnum.deviceTypeEnum) {
                return aurineEvent.eventTypeEnum;
            }
        }
        return EventTypeEnum.OTHER;
    }





}
