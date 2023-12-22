package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 开放平台推送订阅回调类型枚举
 *
 * @author : Qiu
 * @date : 2021 12 09 11:49
 */

@AllArgsConstructor
public enum OpenPushSubscribeCallbackTypeEnum {

    CONFIG("0", "config", "配置类"),
    CASCADE("1", "cascade", "级联入云类"),
    OPERATE("2", "operate", "操作类"),
    COMMAND("3", "command", "指令类"),
    EVENT("4", "event", "事件类"),
    FEEDBACK("5", "feedback", "反馈类"),
    OTHER("9", "other", "其他"),
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
    public static OpenPushSubscribeCallbackTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenPushSubscribeCallbackTypeEnum value : OpenPushSubscribeCallbackTypeEnum.values()) {
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
    public static OpenPushSubscribeCallbackTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenPushSubscribeCallbackTypeEnum value : OpenPushSubscribeCallbackTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
