package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.TencentIntercomRemoteService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: cloudx
 * @description: 腾讯 remote 工厂类
 * @author: 谢泽毅
 * @create: 2021-08-11 14:27
 **/
@Component
public class TencentRemoteServiceFactory implements ApplicationContextAware {

    private static Map<String, TencentIntercomRemoteService> instanceMap;

    /**
     * 获取接口下所有系统的接口调用实现，并注入Map中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, TencentIntercomRemoteService> map = applicationContext.getBeansOfType(TencentIntercomRemoteService.class);
        instanceMap = new HashMap<>();
        map.forEach((key, value) -> instanceMap.put(value.getVersion(), value));
    }

    /**
     * <p>根据对接的系统获取实例</p>
     *
     * @param versionEnum
     * @return
     */
    public static TencentIntercomRemoteService getInstance(VersionEnum versionEnum) {
        if (instanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到TencentRemoteService实例:" + versionEnum.code);
        }
        return instanceMap.get(versionEnum.code);
    }

}
