package com.aurine.cloudx.estate.thirdparty.module.device.platform;

/**
 * <p>
 *  设备参数初始化服务
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/18 10:16
 */
public interface DeviceParamInitService extends DeviceParamConfigService{

    /**
     * <p>
     *  配置预设的默认设备参数到设备上
     * </p>
     *
     * @param deviceId 要配置的设备ID
     * @author: 王良俊
     */
    void configDefaultDeviceParam(String deviceId);
}
