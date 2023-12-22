package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: PersonTypeEnum
 * Description: 人员类型
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/4/21 17:27
 */
@AllArgsConstructor
public enum PersonTypeEnum {
    /**
     * 住户
     */
    PROPRIETOR("1"),
    /**
     * 访客
     */
    VISITOR("3"),
    /**
     * 员工
     */
    STAFF("2");

    public String code;

    /**
     * @param type
     * @return
     */
    public static PersonTypeEnum getEnum(String type) {
        for (PersonTypeEnum value : PersonTypeEnum.values()) {
            if (value.code.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
