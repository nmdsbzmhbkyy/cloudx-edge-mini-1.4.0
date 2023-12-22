package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonitorDeviceTypeEnum {


    FACE_CAMERA("人脸抓拍机","0"),

    BALL_CAMERA("球机","1"),

    GUN_CAMERA("枪机","2");

    /**
     * 设备类型名称
     */
    private final String name;
    /**
     * 设备类型
     */
    private final String type;
}
