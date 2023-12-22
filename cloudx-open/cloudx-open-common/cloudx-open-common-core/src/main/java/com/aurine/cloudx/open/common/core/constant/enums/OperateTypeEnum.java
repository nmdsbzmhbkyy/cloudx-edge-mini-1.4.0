package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 操作类型
 *
 * @author : Qiu
 * @date : 2021 12 14 10:49
 */

@AllArgsConstructor
public enum OperateTypeEnum {

    DELETE("0", "delete", "删除操作"),
    ADD("1", "add", "添加操作"),
    UPDATE("2", "update", "修改操作"),
    SYNC("3","sync","同步操作"),
    STATE("9", "state", "状态操作"),

    NULL("-1", null, "空")
    ;
    
    public String code;
    public String name;
    public String desc;

    public static OperateTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static OperateTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
