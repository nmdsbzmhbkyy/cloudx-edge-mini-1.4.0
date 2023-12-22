package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: 人员扩展属性类型
 * Description: 人员类型
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/25 17:27
 */
@AllArgsConstructor
public enum PersonAttrTypeEnum {
    /**
     * 住户
     */
    PROPRIETOR("1"),
    /**
     * 员工
     */
    PLACE_MANAGE("2");

    public String code;

    /**
     * @param type
     * @return
     */
    public static PersonAttrTypeEnum getEnum(String type) {
        for (PersonAttrTypeEnum value : PersonAttrTypeEnum.values()) {
            if (value.code.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
