package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 呼叫记录通话类型
 *
 * 2022-07-19: create by guwh
 */
@AllArgsConstructor
@Getter
public enum CallTypeEnum {
    CALL_OWNER("0", "呼叫业主"),
    CALL_CENTER("1", "呼叫中心"),
    CALL_PROPERTY("2", "呼叫物业"),
    CALL_FORWARDING("3", "呼叫转移（云电话）");

    public String code;
    public String desc;

    public static CallTypeEnum getByCode(String code) {
        CallTypeEnum[] callTypeEnums = values();
        for (CallTypeEnum callTypeEnum : callTypeEnums) {
            if (callTypeEnum.getCode().equals(code)) {
                return callTypeEnum;
            }
        }
        return null;
    }
}