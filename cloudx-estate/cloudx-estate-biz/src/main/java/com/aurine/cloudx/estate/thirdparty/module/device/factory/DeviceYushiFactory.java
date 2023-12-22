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
 * 置空设备中台工厂
 *
 * @ClassName: DeviceAurineFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:08
 * @Copyright:
 */
@Component
public class DeviceYushiFactory extends AbstractDeviceFactory implements ApplicationContextAware {

    private static DeviceYushiFactory factoryInstance;//工厂单例

    private Map<String, DeviceService> deviceServiceInstanceMap = new HashMap<>();//通行设备各版本实例
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
        DeviceYushiFactory.factoryInstance = applicationContext.getBean(DeviceYushiFactory.class);

        //注入各版本实现

        initBean(DeviceService.class, deviceServiceInstanceMap, applicationContext);

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

    @Override
    public PassWayDeviceService getPassWayDeviceService() {
        return null;
    }

    @Override
    public PerimeterAlarmDeviceService getPerimeterAlarmDeviceService() {
        return null;
    }

    @Override
    public IotDeviceService getIotDeviceService() {
        return null;
    }

    /**
     * 获取设备接口实例
     *
     * @return
     */
    @Override
    public DeviceService getDeviceService() {
        if (deviceServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到Yushi DeviceService 实例:" + versionEnum.code);
        }
        return deviceServiceInstanceMap.get(versionEnum.code);
    }


    /**
     * 初始化bean，并注入工厂
     */
    private <T extends BaseService> void initBean(Class<T> t, Map instanceMap, ApplicationContext applicationContext) throws BeansException {

        Map<String, T> beanMap = applicationContext.getBeansOfType(t);

        beanMap.forEach((key, instance) -> {
            if (instance.getPlatform().equals(PlatformEnum.YUSHI.code)) {
                instanceMap.put(instance.getVersion(), instance);
            }
        });
    }
}
