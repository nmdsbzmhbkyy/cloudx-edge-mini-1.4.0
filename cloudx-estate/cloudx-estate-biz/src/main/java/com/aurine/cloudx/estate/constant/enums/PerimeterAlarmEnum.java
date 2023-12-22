package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: PerimeterAlarmEnum
 * @author: 邹宇 <>
 * @date:  2021年06月23日 上午10:41:23
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum PerimeterAlarmEnum {

    /**
     * 其他告警
     */
    OTHER_ALARM("0","其他告警"),

    /**
     * 入侵告警
     */
    INVADE_ALARM("1","入侵告警"),

    /**
     * 触网告警
     */
    TOUCH_NET_ALARM("2","触网告警"),
    /**
     * 短路告警
     */
    SHORT_CIRCUIT_ALARM("3","短路告警"),


    /**
     * 断路告警
     */
    BREAK_CIRCUIT_ALARM("4","断路告警"),

    /**
     * 防拆告警
     */
    TAMPER_PROOF_ALARM("5","防拆告警"),

    /**
     * 故障告警
     */
    FAULTALARM("6","故障告警");

    /**
     * 报警类型
     */
    private final String type;

    /**
     * 报警名称
     */
    private final String name;

    /**
     * @param type
     * @return
     */
    public static PerimeterAlarmEnum getEnum(String type) {
        for (PerimeterAlarmEnum value : PerimeterAlarmEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
