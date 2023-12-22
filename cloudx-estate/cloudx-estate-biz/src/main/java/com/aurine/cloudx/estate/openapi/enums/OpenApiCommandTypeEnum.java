package com.aurine.cloudx.estate.openapi.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wrm
 * @Date: 2021/12/23 13:36
 * @Package: com.aurine.cloudx.estate.constant.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum OpenApiCommandTypeEnum {

    CLOSE("0", "close", "关闭指令"),
    OPEN("1", "open", "打开指令"),
    CHANGE("2", "change", "改变指令"),
    EMPTY("4", "empty", "清空指令"),
    DOWN("11", "down", "下发指令"),
    DELETE("12", "delete", "删除指令"),

    DELETE_DEVICE("101", "deleteDevice", "删除设备指令"),
    DELETE_HOUSE_PERSON("111", "deleteHousePerson", "删除住户指令"),

    NULL("-1", null, "空");


    public String code;
    public String name;
    public String desc;

    public static OpenApiCommandTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenApiCommandTypeEnum value : OpenApiCommandTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static OpenApiCommandTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenApiCommandTypeEnum value : OpenApiCommandTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
