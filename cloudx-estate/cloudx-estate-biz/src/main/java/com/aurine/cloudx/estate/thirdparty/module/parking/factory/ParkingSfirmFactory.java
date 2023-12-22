package com.aurine.cloudx.estate.thirdparty.module.parking.factory;

import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.ParkingService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@Component
public class ParkingSfirmFactory extends AbstractParkingFactory implements ApplicationContextAware {

    private static ParkingSfirmFactory factoryInstance;//工厂单例
    private Map<String, ParkingService> parkingServiceInstanceMap = new HashMap<>();//车场实例Map
    private ProjectParkingInfo parkingInfo;
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
        ParkingSfirmFactory.factoryInstance = applicationContext.getBean(ParkingSfirmFactory.class);

        //注入各版本实现
        Map<String, ParkingService> map = applicationContext.getBeansOfType(ParkingService.class);
        map.forEach((key, instance) -> {
            if (instance.getPlatform().equals(ParkingAPICompanyEnum.SFIRM.name())) {
                parkingServiceInstanceMap.put(instance.getVersion(), instance);
            }
        });

    }

    /**
     * 获取工厂单例
     *
     * @return
     */
    static public AbstractParkingFactory getFactory(ProjectParkingInfo parkingInfo, VersionEnum versionEnum) {
        factoryInstance.parkingInfo = parkingInfo;
        factoryInstance.versionEnum = versionEnum;
        return factoryInstance;
    }


    /**
     * 获取车场接口实例
     *
     * @param versionEnum
     * @return
     */
    public ParkingService getParkingServiceByVersion(VersionEnum versionEnum) {
        if (parkingServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到Sfirm ParkingService实例:" + versionEnum.code);
        }
        return parkingServiceInstanceMap.get(versionEnum.code);
    }

    /**
     * 获取车场接口实例
     *
     * @return
     */
    @Override
    public ParkingService getParkingService() {
        if (parkingServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到Sfirm ParkingService实例:" + versionEnum.code);
        }
        return parkingServiceInstanceMap.get(versionEnum.code);
    }
}
