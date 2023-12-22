package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.YushiRemoteDeviceService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class YushiRemoteDeviceServiceFactory implements ApplicationContextAware {

    static Map<String, YushiRemoteDeviceService> instanceMap;


    /**
     * 获取接口下所有系统的接口调用实现，并注入Map中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, YushiRemoteDeviceService> map = applicationContext.getBeansOfType(YushiRemoteDeviceService.class);
        instanceMap = new HashMap<>();
        map.forEach((key, value) -> instanceMap.put(value.getVersion(), value));


    }

    /**
     * <p>根据对接的系统获取实例</p>
     *
     * @param versionEnum
     * @return
     */
    public static YushiRemoteDeviceService getInstance(VersionEnum versionEnum) {
        if (instanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 YushiRemoteDeviceService 实例:" + versionEnum.code);
        }
        return instanceMap.get(versionEnum.code);
    }

}
