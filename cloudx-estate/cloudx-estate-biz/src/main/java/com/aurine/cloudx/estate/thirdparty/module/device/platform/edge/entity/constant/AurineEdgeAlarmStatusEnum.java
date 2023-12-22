package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.constant.enums.AlarmEventStatusEnum;
import lombok.AllArgsConstructor;

/**
 * 设备报警状态
 *
 * @description:
 * @author : 邱家标 <qiujb@miligc.com>
 * @date : 2021 11 12 14:15
 * @Copyright:
 */

@AllArgsConstructor
public enum AurineEdgeAlarmStatusEnum {

    /*
     * 报警 - 未处理
     * 未报警 - 已处理
     * */

    UNPROCESSED("1", "报警", AlarmEventStatusEnum.UNPROCESSED.code),
    PROCESSED("0", "未报警", AlarmEventStatusEnum.PROCESSED.code),

    ;

    public String code;
    public String desc;
    public String cloudCode; // 云平台的字典编码

    public static AurineEdgeAlarmStatusEnum getByCode(String code) {
        AurineEdgeAlarmStatusEnum[] statusEnums = values();
        for (AurineEdgeAlarmStatusEnum statusEnum : statusEnums) {
            if (statusEnum.code().equals(code)) {
                return statusEnum;
            }

        }
        return UNPROCESSED;
    }

    private String code() {
        return this.code;
    }
}
