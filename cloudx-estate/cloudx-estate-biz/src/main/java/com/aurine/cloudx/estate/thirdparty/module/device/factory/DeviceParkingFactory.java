package com.aurine.cloudx.estate.thirdparty.module.device.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/15 11:20
 * @Copyright:
 */
@Component
public class DeviceParkingFactory extends AbstractDeviceFactory implements ApplicationContextAware {

    private static DeviceParkingFactory factoryInstance;//工厂单例

    private Map<String, PassWayDeviceService> passWayDeviceServiceInstanceMap = new HashMap<>();//通行设备各版本实例
    private Map<String, DeviceService> deviceServiceInstanceMap = new HashMap<>();//设备各版本实例
    private Map<String, PerimeterAlarmDeviceService> perimeterAlarmDeviceServiceMap = new HashMap<>();//周界告警设备实例
    private Map<String, IotDeviceService> iotDeviceServiceMap = new HashMap<>();// 泛感设备实例
    private VersionEnum versionEnum;


    /**
     * 初始化注入Bean
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //注入工厂实现
        DeviceParkingFactory.factoryInstance = applicationContext.getBean(DeviceParkingFactory.class);

        //注入各版本实现

        //区口机，梯口机
        initBean(PassWayDeviceService.class, passWayDeviceServiceInstanceMap, applicationContext);
        initBean(DeviceService.class, deviceServiceInstanceMap, applicationContext);
        initBean(PerimeterAlarmDeviceService.class, perimeterAlarmDeviceServiceMap, applicationContext);

        // 泛感设备

        initBean(IotDeviceService.class, iotDeviceServiceMap, applicationContext);


        //室内机

        //中心机

        //编码设备

        //监控设备

    }

    /**
     * 获取工厂单例
     *
     * @return
     */
    static public AbstractDeviceFactory getFactory(VersionEnum versionEnum) {
        factoryInstance.versionEnum = versionEnum;
        return factoryInstance;
    }


    /**
     * 获取区口机接口实例
     *
     * @return
     */
    @Override
    public PassWayDeviceService getPassWayDeviceService() {
        if (passWayDeviceServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到AurineEdge PassWayDeviceService 实例:" + versionEnum.code);
        }
        return passWayDeviceServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取周界告警设备接口实例
     *
     * @return
     */
    @Override
    public PerimeterAlarmDeviceService getPerimeterAlarmDeviceService() {
        if (passWayDeviceServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到AurineEdge PerimeterAlarmDeviceService 实例:" + versionEnum.code);
        }
        return perimeterAlarmDeviceServiceMap.get(versionEnum.code);
    }

    @Override
    public IotDeviceService getIotDeviceService() {
        if (iotDeviceServiceMap.get(versionEnum.code) == null) {
            throw new RuntimeException("未找到AurineEdge IotDeviceService 实例:" + versionEnum.code);
        }
        return iotDeviceServiceMap.get(versionEnum.code);
    }

    /**
     * 获取设备接口实例
     *
     * @return
     */
    @Override
    public DeviceService getDeviceService() {
        if (deviceServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到AurineEdge DeviceService 实例:" + versionEnum.code);
        }
        return deviceServiceInstanceMap.get(versionEnum.code);
    }


    /**
     * 初始化bean，并注入工厂
     */
    private <T extends BaseService> void initBean(Class<T> t, Map instanceMap, ApplicationContext applicationContext) throws BeansException {

        Map<String, T> beanMap = applicationContext.getBeansOfType(t);

        beanMap.forEach((key, instance) -> {
            if (instance.getPlatform().equals(PlatformEnum.AURINE_PARKING.code)) {
                instanceMap.put(instance.getVersion(), instance);
            }
        });
    }
}
