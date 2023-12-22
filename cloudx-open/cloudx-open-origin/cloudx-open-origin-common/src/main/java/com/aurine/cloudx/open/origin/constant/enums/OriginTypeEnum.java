package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 来源类型(OriginTypeEnum)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/8 14:20
 */
@AllArgsConstructor
public enum  OriginTypeEnum {
    /**
     * web
     */
    WEB("1"),
    /**
     * 小程序
     */
    WECHAT("2"),
    /**
     * app
     */
    APP("3");

    public String code;
}
