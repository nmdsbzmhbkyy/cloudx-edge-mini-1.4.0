package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>设备接入方式枚举</p>
 *
 * @ClassName: FrameTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/26 11:37
 * @Copyright:
 */
@AllArgsConstructor
public enum DeviceAccessMethodEnum {
    /**
     * 手动
     */
    MANUAL("2"),
    /**
     * 自动
     */
    AUTO("1"),
    /**
     * 未定义
     */
    UNDEFINED("9");

    public String code;
}
