package com.aurine.cloudx.estate.thirdparty.module.device.factory.param;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamDataService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamFormService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.service.AbstractParamServiceByHuawei;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>
 * 设备参数服务的工厂类（华为中台）
 * </p>
 * @author : 王良俊
 * @date : 2021-07-08 10:22:06
 */
@Component
public class DeviceHuaweiParamServiceFactory implements ApplicationContextAware, DeviceParamServiceFactory {

    private static final Map<String, AbstractParamServiceByHuawei> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractParamServiceByHuawei> beansOfType = applicationContext.getBeansOfType(AbstractParamServiceByHuawei.class);
        Collection<AbstractParamServiceByHuawei> values = beansOfType.values();
        values.forEach(abstractParamService -> {
            Set<DeviceManufactureEnum> applicableDeviceProducts = abstractParamService.getApplicableDeviceProducts();
            if (CollectionUtil.isNotEmpty(applicableDeviceProducts)) {
                applicableDeviceProducts.forEach(deviceProductEnum -> {
                    beanMap.put(deviceProductEnum.code, abstractParamService);
                });
            }
        });
    }

    @Override
    public DeviceParamConfigService getParamConfigService(String manufacture, String deviceType) {
        DeviceManufactureEnum deviceManufactureEnum = DeviceManufactureEnum.getByManufactureAndDeviceType(manufacture, deviceType);
        return beanMap.get(deviceManufactureEnum.code);
    }

    @Override
    public DeviceParamFormService getParamFormService(String manufacture, String deviceType) {
        DeviceManufactureEnum deviceManufactureEnum = DeviceManufactureEnum.getByManufactureAndDeviceType(manufacture, deviceType);
        return beanMap.get(deviceManufactureEnum.code);
    }

    @Override
    public DeviceParamDataService getParamDataService(String manufacture, String deviceType) {
        DeviceManufactureEnum deviceManufactureEnum = DeviceManufactureEnum.getByManufactureAndDeviceType(manufacture, deviceType);
        return beanMap.get(deviceManufactureEnum.code);
    }

    @Override
    public PlatformEnum getPlatformEnum() {
        return PlatformEnum.HUAWEI_MIDDLE;
    }
}
