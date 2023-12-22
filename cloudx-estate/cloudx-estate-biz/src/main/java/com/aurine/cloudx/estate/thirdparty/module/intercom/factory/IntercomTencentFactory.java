package com.aurine.cloudx.estate.thirdparty.module.intercom.factory;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.BaseService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.IntercomService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: cloudx
 * @description: 腾讯 云对讲 工厂
 * @author: 谢泽毅
 * @create: 2021-08-11 11:51
 **/
@Component
public class IntercomTencentFactory extends AbstractIntercomFactory implements ApplicationContextAware {

    private static IntercomTencentFactory factoryInstance;//工厂单例

    private Map<String, IntercomService> intercomServiceInstanceMap = new HashMap<>();
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
        IntercomTencentFactory.factoryInstance = applicationContext.getBean(IntercomTencentFactory.class);

        //注入各版本实现
        initBean(IntercomService.class, intercomServiceInstanceMap, applicationContext);


    }

    /**
     * 获取工厂单例
     *
     * @return
     */
    static public AbstractIntercomFactory getFactory(VersionEnum versionEnum) {
        factoryInstance.versionEnum = versionEnum;
        return factoryInstance;
    }



    /**
     * 获取设备接口实例
     *
     * @return
     */
    @Override
    public IntercomService getIntercomService() {
        if (intercomServiceInstanceMap.get(versionEnum.code) == null){
            throw new RuntimeException("未找到腾讯 IntercomUserDeviceService 实例:" + versionEnum.code);
        }
        return intercomServiceInstanceMap.get(versionEnum.code);
    }


    /**
     * 初始化bean，并注入工厂
     */
    private <T extends BaseService> void initBean(Class<T> t, Map instanceMap, ApplicationContext applicationContext) throws BeansException {

        Map<String, T> beanMap = applicationContext.getBeansOfType(t);

        beanMap.forEach((key, instance) -> {
            if (instance.getPlatform().equals(PlatformEnum.INTERCOM_TENCENT.code)) {
                instanceMap.put(instance.getVersion(), instance);
            }
        });
    }
}
