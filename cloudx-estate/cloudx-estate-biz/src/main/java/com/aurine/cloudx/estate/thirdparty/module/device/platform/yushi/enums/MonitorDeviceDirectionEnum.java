package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonitorDeviceDirectionEnum {


    IN("进","0"),

    OUT("出","1"),

    UNKNOW("未知","2");

    /**
     * 设备类型名称
     */
    private final String name;
    /**
     * 设备类型
     */
    private final String type;
}
