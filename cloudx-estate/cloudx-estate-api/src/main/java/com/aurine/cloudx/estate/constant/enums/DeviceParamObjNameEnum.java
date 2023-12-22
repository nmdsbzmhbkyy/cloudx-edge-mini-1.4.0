package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceParamObjNameEnum {

    PARKING_DEVICE_SCREEN("parkingDeviceScreen"),
    PARKING_DEVICE_VOICE("parkingDeviceVoice"),
    PARKING_OTHER_SETTING("otherSetting"),
    PARKING_DISPLAY_SETTING("displaySetting"),
    ;
    String name;

}
