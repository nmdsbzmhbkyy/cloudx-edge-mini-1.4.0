package com.aurine.cloudx.dashboard.constant;

import lombok.AllArgsConstructor;

/**
 * @description: 版本号枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-22
 * @Copyright:
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
     * @param code
     * @return
     */
    public static VersionEnum getByCode(String code) {
        for (VersionEnum value : VersionEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param number
     * @return
     */
    public static VersionEnum getByNumber(String number) {
        for (VersionEnum value : VersionEnum.values()) {
            if (value.number.equals(number)) {
                return value;
            }
        }
        return null;
    }

}
