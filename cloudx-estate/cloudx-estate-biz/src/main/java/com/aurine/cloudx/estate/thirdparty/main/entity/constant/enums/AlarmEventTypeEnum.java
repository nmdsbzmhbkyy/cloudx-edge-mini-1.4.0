package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @description: 4.0 告警事件类型枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-16
 * @Copyright:
 */
@AllArgsConstructor
public enum AlarmEventTypeEnum {


    A002001("A002001", "3次密码错误告警", "errorPwdAlarm"),
    A002002("A002002", "长时间逗留报警", "doorOpenTimeoutAlarm"),
    A002004("A002004", "挟持开门", "holdPwdAlarm"),
    A002005("A002005", "强行开门", "doorOpenTimeoutAlarm"),
    A002007("A002007", "防拆报警", "destoryAlarm"),


    A003001("A003001", "3次密码错误告警", "errorPwdAlarm"),
    A003002("A003002", "长时间逗留报警", "doorOpenTimeoutAlarm"),
    A003004("A003004", "挟持开门", "holdPwdAlarm"),
    A003005("A003005", "强行开门", "doorOpenTimeoutAlarm"),
    A003007("A003007", "防拆报警", "destoryAlarm"),


    OTHER("", "未知事件类型", null);


    public String eventTypeId;
    public String eventTypeName;
    public String field;


    public static AlarmEventTypeEnum getByValue(String value) {
        for (AlarmEventTypeEnum alarmEventTypeEnum : AlarmEventTypeEnum.values()) {
            if (alarmEventTypeEnum.eventTypeId.equals(value)) {
                return alarmEventTypeEnum;
            }
        }
        return null;
    }
}
