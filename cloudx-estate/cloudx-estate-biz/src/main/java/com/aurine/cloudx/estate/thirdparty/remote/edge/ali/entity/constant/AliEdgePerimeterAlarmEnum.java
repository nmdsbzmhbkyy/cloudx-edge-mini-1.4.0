package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.constant;

import com.aurine.cloudx.estate.constant.enums.PerimeterAlarmEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AliEdgePerimeterAlarmEnum {

    /**
     * 其他告警
     */
    OTHER_ALARM(PerimeterAlarmEnum.OTHER_ALARM.getType(), "0", "其他告警"),

    /**
     * 入侵告警
     */
    INVADE_ALARM(PerimeterAlarmEnum.INVADE_ALARM.getType(),  "1", "入侵告警"),

    /**
     * 触网告警
     */
    TOUCH_NET_ALARM(PerimeterAlarmEnum.TOUCH_NET_ALARM.getType(),"2", "触网告警"),
    /**
     * 短路告警
     */
    SHORT_CIRCUIT_ALARM(PerimeterAlarmEnum.SHORT_CIRCUIT_ALARM.getType(), "3", "短路告警"),


    /**
     * 断路告警
     */
    BREAK_CIRCUIT_ALARM(PerimeterAlarmEnum.BREAK_CIRCUIT_ALARM.getType(), "4", "断路告警"),

    /**
     * 防拆告警
     */
    TAMPER_PROOF_ALARM(PerimeterAlarmEnum.TAMPER_PROOF_ALARM.getType(),"5", "防拆告警"),

    /**
     * 故障告警
     */
    FAULTALARM(PerimeterAlarmEnum.FAULTALARM.getType(),"6", "故障告警");

    /**
     * 报警类型
     */
    private final String cloudCode;
    /**
     * 报警类型
     */
    private final String aliCode;

    /**
     * 报警名称
     */
    private final String name;

    /**
     * @param aliCode
     * @return
     */
    public static AliEdgePerimeterAlarmEnum getEnumByAliCode(String aliCode) {
        for (AliEdgePerimeterAlarmEnum value : AliEdgePerimeterAlarmEnum.values()) {
            if (value.getAliCode().equals(aliCode)) {
                return value;
            }
        }
        return null;
    }
}
