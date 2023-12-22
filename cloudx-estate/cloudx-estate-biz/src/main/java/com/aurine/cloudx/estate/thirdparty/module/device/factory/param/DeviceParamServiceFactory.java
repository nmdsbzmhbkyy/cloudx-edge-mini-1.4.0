package com.aurine.cloudx.estate.thirdparty.module.device.factory.param;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamDataService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamFormService;

/**
 * <p>参数服务工厂</p>
 *
 * @author : 王良俊
 * @date : 2021-10-20 09:18:02
 */
public interface DeviceParamServiceFactory {

    /**
     * <p>获取参数服务</p>
     *
     * @param manufacture 设备厂商
     * @param deviceType  设备类型ID
     * @return 参数服务类
     * @author : 王良俊
     */
//    DeviceParamConfigService getInstance(String manufacture, String deviceType);

    /**
     * <p>获取设备参数配置服务</p>
     *
     * @param manufacture 设备厂商
     * @param deviceType  设备类型ID
     * @return 参数服务类
     * @author : 王良俊
     */
    DeviceParamConfigService getParamConfigService(String manufacture, String deviceType);

    /**
     * <p>获取设备参数表单服务</p>
     *
     * @param manufacture 设备厂商
     * @param deviceType  设备类型ID
     * @return 参数服务类
     * @author : 王良俊
     */
    DeviceParamFormService getParamFormService(String manufacture, String deviceType);

    /**
     * <p>获取设备参数数据服务</p>
     *
     * @param manufacture 设备厂商
     * @param deviceType  设备类型ID
     * @return 参数服务类
     * @author : 王良俊
     */
    DeviceParamDataService getParamDataService(String manufacture, String deviceType);

    /**
     * <p>获取工厂适用的平台</p>
     *
     */
    PlatformEnum getPlatformEnum();

    /**
     * <p>获取中台版本</p>
     *
     */
//    PlatformEnum getVersion();

}
