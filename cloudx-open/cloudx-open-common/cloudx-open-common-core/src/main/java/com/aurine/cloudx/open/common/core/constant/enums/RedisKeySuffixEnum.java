package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 存入Redis的Key值后缀枚举
 *
 * @author : Qiu
 * @date : 2022 01 24 10:48
 */

@AllArgsConstructor
public enum RedisKeySuffixEnum {

    APP_INFO("appInfo", "应用信息"),
    PROJECT_INFO("projectInfo", "项目信息"),
    CALLBACK_LIST("callbackList", "回调列表"),

    ;

    public String suffix;
    public String desc;

    public static RedisKeySuffixEnum getBySuffix(String suffix) {
        if (StringUtils.isEmpty(suffix)) return null;
        for (RedisKeySuffixEnum value : RedisKeySuffixEnum.values()) {
            if (value.suffix.equals(suffix)) {
                return value;
            }
        }
        return null;
    }
}
