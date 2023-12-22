package com.aurine.cloudx.estate.thirdparty.module.device.factory.param;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamDataService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamFormService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service.AbstractParamServiceByEdge;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 设备参数服务的工厂类（边缘网关中台）
 * </p>
 * @author : 王良俊
 * @date : 2021-07-08 10:22:06
 */
@Component
public class DeviceEdgeParamServiceFactory implements ApplicationContextAware, DeviceParamServiceFactory {

    private static final Map<String, AbstractParamServiceByEdge> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractParamServiceByEdge> beansOfType = applicationContext.getBeansOfType(AbstractParamServiceByEdge.class);
        Collection<AbstractParamServiceByEdge> values = beansOfType.values();
        values.forEach(abstractParamService -> {
            Set<DeviceManufactureEnum> applicableDeviceProducts = abstractParamService.getApplicableDeviceProducts();
            if (CollectionUtil.isNotEmpty(applicableDeviceProducts)) {
                applicableDeviceProducts.forEach(deviceProductEnum -> {
                    beanMap.put(deviceProductEnum.code, abstractParamService);
                });
            }
        });
    }

    /**
    * <p>
    * 通过设备制造厂商和设备类型ID获取对应的实例
    * </p>
    *
    * @param deviceType 设备类型ID
    * @param manufacture 设备制造厂商（可能是中文）
    * @return 参数服务实例
    */


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
        return PlatformEnum.AURINE_EDGE_MIDDLE;
    }
}
