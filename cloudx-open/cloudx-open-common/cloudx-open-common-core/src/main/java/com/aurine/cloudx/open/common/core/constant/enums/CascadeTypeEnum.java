package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 级联入云类型
 *
 * @author : Qiu
 * @date : 2021 12 23 11:15
 */

@AllArgsConstructor
public enum CascadeTypeEnum {

    APPLY("0", "apply", "申请级联入云"),
    REVOKE("1", "revoke", "撤销级联入云"),
    ACCEPT("2", "accept", "同意级联入云"),
    REJECT("3", "reject", "拒绝级联入云"),
    MQTT("9", "mqtt", "mqtt相关配置"),

    ADD("11", "add", "添加"),
    UPDATE("12", "update", "修改"),
    DELETE("13", "delete", "删除"),

    NULL("-1", null, "空")
    ;

    public String code;
    public String name;
    public String desc;

    public static CascadeTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (CascadeTypeEnum value : CascadeTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static CascadeTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (CascadeTypeEnum value : CascadeTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
