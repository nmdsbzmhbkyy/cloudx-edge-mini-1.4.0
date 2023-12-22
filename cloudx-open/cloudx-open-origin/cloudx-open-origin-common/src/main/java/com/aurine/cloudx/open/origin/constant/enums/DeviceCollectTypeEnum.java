package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * (DeviceCollectTypeEnum)项目设备对接枚举
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/18 9:32
 */
@Getter
@AllArgsConstructor
public enum DeviceCollectTypeEnum {

    FACE("1", "人脸采集"),
    ID_CARD("2", "身份采集"),
    VIDEO("3", "视频"),
    POLICE("4", "公安"),
    WR20("5", "WR20"),
    ZT("6", "中台");
    public String code;
    public String name;
}
