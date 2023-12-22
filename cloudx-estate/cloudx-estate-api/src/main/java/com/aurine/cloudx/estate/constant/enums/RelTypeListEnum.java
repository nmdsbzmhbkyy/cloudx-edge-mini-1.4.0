package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-02-08 10:50
 */
@AllArgsConstructor
public enum RelTypeListEnum {
    /**
     * 临时车
     */
    TEMP("0","公共"),
    /**
     * 月租车
     */
    MONTH("2","租赁"),
    /**
     * 免费车
     */
    FREE("1","自有");

    public String code;
    public String value;

    public static RelTypeListEnum getByCode(String code) {
        for (RelTypeListEnum value : RelTypeListEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
