package com.aurine.cloud.code.entity.enums;

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

    V1("v1", "v1.0","福建闵政通1.0版本");



    public String code;
    public String placeName;
    public  String desc;

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
     * @param placeName
     * @return
     */
    public static VersionEnum getByplaceName(String placeName) {
        for (VersionEnum value : VersionEnum.values()) {
            if (value.placeName.equals(placeName)) {
                return value;
            }
        }
        return null;
    }

}
