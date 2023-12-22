package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * CloudxOpen版本定义
 *
 * @author : Qiu
 * @date : 2021 12 13 16:10
 */

@AllArgsConstructor
public enum VersionEnum {

    V1("v1", "1"),
    V2("v2", "2"),
    V3("v3", "3"),
    V4("v4", "4"),
    V5("v5", "5"),
    V6("v6", "6"),
    V7("v7", "7"),
    V8("v8", "8"),
    V9("v9", "9");

    public String code;
    public String number;

    /**
     * 根据code获取
     * @param code
     * @return
     */
    public static VersionEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (VersionEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据number获取
     * @param number
     * @return
     */
    public static VersionEnum getByNumber(String number) {
        if (StringUtils.isEmpty(number)) return null;
        for (VersionEnum value : values()) {
            if (value.number.equals(number)) {
                return value;
            }
        }
        return null;
    }

}
