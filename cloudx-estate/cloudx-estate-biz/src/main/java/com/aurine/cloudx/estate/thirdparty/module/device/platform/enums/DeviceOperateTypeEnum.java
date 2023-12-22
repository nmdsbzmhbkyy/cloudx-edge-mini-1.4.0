package com.aurine.cloudx.estate.thirdparty.module.device.platform.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 设备厂商和设备类型的枚举，用于判断
 * </p>
 * @author : 王良俊
 * @date : 2021-07-08 10:23:47
 */
@AllArgsConstructor
public enum DeviceOperateTypeEnum {

    switchLight("路灯开关控制");

    /**
     * 操作名
     * */
    public String operateName;

}
