package com.aurine.cloudx.estate.constant.enums;

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
    PROPRIETOR("1","住户"),
    /**
     * 访客
     */
    VISITOR("3","访客"),
    /**
     * 员工
     */
    STAFF("2","员工"),
    /**
     * 英蓝
     */
    BLUE("4","英蓝"),
    /**
     * 黑名单
     */
    BLACKLIST("10","黑名单");

    public String code;
    public String type;

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

    /**
     * @param type
     * @return
     */
    public static String getType(String type) {
        for (PersonTypeEnum value : PersonTypeEnum.values()) {
            if (value.code.equals(type)) {
                return value.type;
            }
        }
        return null;
    }

}
