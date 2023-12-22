package com.aurine.cloudx.estate.thirdparty.module.device.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.alarm.service.AbstractDeviceEventService;
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
 * 设备事件处理服务工厂类
 * </p>
 * @author : 王良俊
 * @date : 2021-08-06 17:03:24
 */
@Component
public class DeviceEventServiceFactory implements ApplicationContextAware {

    private static final Map<String, AbstractDeviceEventService> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractDeviceEventService> beansOfType = applicationContext.getBeansOfType(AbstractDeviceEventService.class);
        Collection<AbstractDeviceEventService> values = beansOfType.values();
        values.forEach(abstractDeviceEventService -> {
            Set<DeviceManufactureEnum> applicableDeviceProducts = abstractDeviceEventService.getApplicableDeviceProducts();
            if (CollectionUtil.isNotEmpty(applicableDeviceProducts)) {
                applicableDeviceProducts.forEach(deviceProductEnum -> {
                    beanMap.put(deviceProductEnum.code, abstractDeviceEventService);
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
    public static AbstractDeviceEventService getInstance(String manufacture, String deviceType) {
        DeviceManufactureEnum deviceManufactureEnum = DeviceManufactureEnum.getByManufactureAndDeviceType(manufacture, deviceType);
        return beanMap.get(deviceManufactureEnum.code);
    }

}
