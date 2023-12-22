package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 数据来源
 * @ClassName: DataOriginEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-09 14:08
 * @Copyright:
 */
@AllArgsConstructor
public enum DataOriginEnum {
    /**
     * 未处理
     */
    WEB("1"),
    /**
     * 微信小程序
     */
    WECHAT("2"),
    /**
     * APP
     */
    APP("3"),
    /**
     * 第三方平台
     */
    THIRD_PARTY("4");

    public String code;
}
