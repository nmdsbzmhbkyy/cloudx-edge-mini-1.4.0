package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 第三方设备类型ID枚举 
 * </p>
 * @author : 王良俊
 * @date : 2021-07-19 13:53:37
 */
@AllArgsConstructor
public enum ThirdPartyDeviceTypeEnum {
    LEVEL_GAUGE("27"),
    WATER_METER("7"),
    MANHOLE_COVER("130");
    
    public String devType;
}
