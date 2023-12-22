package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.FujicaRemoteParkingCallbackService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>富士车场回调接口工厂</p>
 *
 * @ClassName: SfirmParkingCallbackFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-05-21 13:54
 * @Copyright:
 */
@Component
public class FujicaParkingCallbackFactory implements ApplicationContextAware {
    static Map<String, FujicaRemoteParkingCallbackService> instanceMap;

    /**
     * 获取接口下所有系统的接口调用实现，并注入Map中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, FujicaRemoteParkingCallbackService> map = applicationContext.getBeansOfType(FujicaRemoteParkingCallbackService.class);
        instanceMap = new HashMap<>();
        map.forEach((key, value) -> instanceMap.put(value.getVersion(), value));
    }

    /**
     * <p>根据对接的系统获取实例</p>
     *
     * @param versionEnum
     * @return
     */
    public static FujicaRemoteParkingCallbackService getInstance(VersionEnum versionEnum) {
        if (instanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到 FujicaRemoteParkingCallbackService 实例:" + versionEnum.name());
        }
        return instanceMap.get(versionEnum.code);
    }
}
