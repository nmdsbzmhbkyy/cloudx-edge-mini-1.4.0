package com.aurine.cloudx.estate.thirdparty.module.device.policy.enums;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.policy.entity.QiyuStreetLightPolicy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DevicePolicyEnum {

    /**
     * 骐驭路灯策略
     * */
    SHANGRUN_STREET_LIGHTS(DeviceManufactureEnum.QIYU_SMART_STREET_LIGHT, QiyuStreetLightPolicy.class);

    public DeviceManufactureEnum deviceManufactureEnum;

    public Class clazz;

    /**
    * <p>
    * 获取策略类的class
    * </p>
    *
    * @param
    * @return
    * @author: 王良俊
    */
    public static Class getClazz(DeviceManufactureEnum deviceManufactureEnum) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].deviceManufactureEnum.equals(deviceManufactureEnum)) {
                return values()[i].clazz;
            }
        }
        return null;
    }

}
