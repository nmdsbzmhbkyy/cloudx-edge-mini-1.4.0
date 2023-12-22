package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-26 15:02
 */
@AllArgsConstructor
public enum  AurineAlarmEnum {
    //事件代码
    //1、挟持开门；
    ALERT_OPENDOOR("1", "挟持开门"),
    //2、3次密码错误告警；
    ALERT_THREE_WRONG_PASSWORD("2", "3次密码错误告警"),
    //3、长时间逗留报警；
    ALERT_HOLD_TOO_LONG("3", "长时间逗留报警"),
    //4、3次刷卡错误告警；
    ALERT_THREE_WRONG_CARD("4", "3次刷卡错误告警"),
    //5、3次指纹错误告警；
    ALERT_THREE_WRONG_FINGER("5", "3次指纹错误告警"),
    //10、强行开门报警；
    ALERT_OPENDOOR_FORCE("10", "强行开门报警"),
    //11、门未关报警；
    ALERT_NO_CLOSE("11", "门未关报警"),
    //12、防拆报警；
    ALERT_BREAK("12", "防拆报警"),
    //20、黑名单告警；
    ALERT_OTHER_ERROR("20", "黑名单告警"),

    OTHER(null,"未知告警");
    public String code;
    public String desc;

    public static AurineAlarmEnum getByCode(String code) {
        AurineAlarmEnum[] entityEnums = values();
        for (AurineAlarmEnum entity : entityEnums) {
            if (entity.code.equals(code)) {
                return entity;
            }
        }
        return AurineAlarmEnum.OTHER;
    }
}
