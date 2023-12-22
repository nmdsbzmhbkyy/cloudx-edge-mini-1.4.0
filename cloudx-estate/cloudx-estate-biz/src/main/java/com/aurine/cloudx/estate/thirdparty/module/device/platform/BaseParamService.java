package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;

import java.util.Set;

/**
 * <p>参数服务基类</p>
 *
 * @author 王良俊
 * @date "2022/4/27"
 */
public interface BaseParamService {

    /**
     * <p>获取当前支持的设备枚举</p>
     *
     * @return 支持的设备枚举列表
     */
    Set<DeviceManufactureEnum> getApplicableDeviceProducts();
}
