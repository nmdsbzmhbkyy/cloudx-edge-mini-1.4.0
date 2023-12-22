package com.aurine.cloudx.estate.openapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wrm
 * @Date: 2021/12/29 19:29
 * @Package: com.aurine.cloudx.estate.constant.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum OpenApiCascadeTypeEnum {
    /**
     * null
     */
    NULL("-1", null, "空"),
    APPLY("0", "apply", "申请级联入云"),
    REVOKE("1", "revoke", "撤销级联入云"),
    ACCEPT("2", "accept", "同意级联入云"),
    REJECT("3", "reject", "拒绝级联入云"),
    MQTT("9", "mqtt", "mqtt相关配置"),

    ADD("11", "add", "添加"),
    UPDATE("12", "update", "修改"),
    DELETE("13", "delete", "删除"),

    ;

    public String code;
    public String name;
    public String desc;

    public static OpenApiCascadeTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenApiCascadeTypeEnum value : OpenApiCascadeTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static OpenApiCascadeTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenApiCascadeTypeEnum value : OpenApiCascadeTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}