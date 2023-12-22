package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-19 17:28
 */
@Getter
@AllArgsConstructor
public enum DecidedEnum {
    /**
     * 车键
     */
    CAR("1", "carKey"),
    /**
     * 车键
     */
    PEOPLE("2", "peopleKey"),
    /**
     * 车键
     */
    ALARM("3", "alertKey"),
    /**
     * 设备
     */
    DEVICE("4", "deviceKey");

    /**
     * 状态类型
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String key;

    /**
     * @param code
     * @return
     */
    public static String getEnum(String code) {
        DecidedEnum[] enums = values();
        for (DecidedEnum decidedEnum : enums) {
            if (decidedEnum.code.equals(code)) {
                return decidedEnum.key;
            }
        }
        return null;
    }

}
