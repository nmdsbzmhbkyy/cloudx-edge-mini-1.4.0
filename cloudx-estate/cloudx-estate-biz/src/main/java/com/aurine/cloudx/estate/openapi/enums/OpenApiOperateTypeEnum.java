package com.aurine.cloudx.estate.openapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wrm
 * @Date: 2021/12/13 18:11
 * @Package: com.aurine.cloudx.estate.constant.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum OpenApiOperateTypeEnum {
    /**
     * null
     */
    NULL("-1", null, "空"),
    DELETE("0", "delete", "删除操作"),
    ADD("1", "add", "添加操作"),
    UPDATE("2", "update", "修改操作"),
    STATE("9", "state", "状态操作"),

    ;

    public String code;
    public String name;
    public String desc;

    public static OpenApiOperateTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenApiOperateTypeEnum value : OpenApiOperateTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static OpenApiOperateTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenApiOperateTypeEnum value : OpenApiOperateTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
