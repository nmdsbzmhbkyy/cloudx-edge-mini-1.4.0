package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 开放平台属性名称枚举
 *
 * @author : Qiu
 * @date : 2022 01 20 15:56
 */

@AllArgsConstructor
public enum OpenFieldEnum {

    APP_ID("appId", String.class, "应用ID"),
    PROJECT_UUID("projectUUID", String.class, "项目UUID"),
    TENANT_ID("tenantId", Integer.class, "租户ID"),
    PROJECT_ID("projectId", Integer.class, "项目ID"),

    ;

    public String name;
    public Class type;
    public String desc;

    /**
     * 根据name获取枚举
     *
     * @param name
     * @return
     */
    public static OpenFieldEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenFieldEnum value : OpenFieldEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
