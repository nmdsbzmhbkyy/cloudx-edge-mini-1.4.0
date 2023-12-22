package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;


/**
 * @ClassName: CertmediaTypeEnum
 * @author: 王良俊 <>
 * @date:  2020年06月08日 下午15:03:23
 * @Copyright:
*/
@AllArgsConstructor
public enum CertmediaTypeEnum {

    /**
     * 指纹
     */
    Finger("1"),
    /**
     * 人脸
     */
    Face("2"),
    /**
     * card
     */
    Card("3"),
    /**
     * 密码
     */
    Password("4");

    public String code;

    public static CertmediaTypeEnum getEnumByCode(String code) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].code.equals(code)) {
                return values()[i];
            }
        }
        return null;
    }

}
