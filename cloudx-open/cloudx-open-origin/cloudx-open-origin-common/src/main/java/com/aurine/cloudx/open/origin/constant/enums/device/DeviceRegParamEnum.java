package com.aurine.cloudx.open.origin.constant.enums.device;

import lombok.AllArgsConstructor;

/**
 * <p>设备异常处理参数容器的参数名</p>
 * @author : 王良俊
 * @date : 2021-09-24 16:22:02
 */
@AllArgsConstructor
public enum DeviceRegParamEnum {

    /**
     * 默认失败事件
     */
    IPV4("IPV4"),
    /**
     * MAC地址
     */
    MAC("MAC"),
    /**
     * 设备编号
     */
    DEVICE_NO("DEVICE_NO"),
    ;

    /**
     * 对应的code
     */
    public String code;

    

}
