package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 版本号
 *
 * @ClassName: VersionEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/23 10:12
 * @Copyright:
 */
@AllArgsConstructor
public enum VersionEnum {

    V1("v1", "1"),
    V2("v2", "2"),
    V2_1("v2.1", "2.1");

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
