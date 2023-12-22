package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 开放平台推送订阅回调方式枚举
 *
 * @author : Qiu
 * @date : 2022 07 26 11:54
 */

@AllArgsConstructor
public enum OpenPushSubscribeCallbackModeEnum {

    URL("0", "url", "url方式"),
    TOPIC("1", "topic", "topic方式"),
    ;

    public String code;
    public String name;
    public String desc;

    /**
     * 根据code获取枚举
     *
     * @param code
     * @return
     */
    public static OpenPushSubscribeCallbackModeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenPushSubscribeCallbackModeEnum value : OpenPushSubscribeCallbackModeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据name获取枚举
     *
     * @param name
     * @return
     */
    public static OpenPushSubscribeCallbackModeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenPushSubscribeCallbackModeEnum value : OpenPushSubscribeCallbackModeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
