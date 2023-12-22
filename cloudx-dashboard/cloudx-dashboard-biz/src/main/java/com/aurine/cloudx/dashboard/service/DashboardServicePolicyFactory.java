package com.aurine.cloudx.dashboard.service;

import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Dashboard 策略工厂
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@Component
public class DashboardServicePolicyFactory implements ApplicationContextAware {

    private static DashboardServicePolicyFactory factoryInstance;//工厂单例

    private Map<String, DashboardService> serviceInstanceMap = new HashMap<>();

    /**
     * 初始化注入Bean
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DashboardServicePolicyFactory.factoryInstance = applicationContext.getBean(DashboardServicePolicyFactory.class);
        initBean(DashboardService.class, serviceInstanceMap, applicationContext);
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static DashboardService getService(String serviceName, String version) {

        if (StringUtils.isNotEmpty(serviceName) && StringUtils.isNotEmpty(version)) {

            String key = serviceName.toUpperCase() + "_" + version.toUpperCase();

            if (factoryInstance.serviceInstanceMap.get(key) == null) throw new DashboardException(DashboardErrorEnum.NOT_FOUND);

            return factoryInstance.serviceInstanceMap.get(key);

        } else {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_TYPE_INVALID);
        }

    }


    /**
     * 初始化bean，并注入工厂
     */
    private <T extends DashboardService> void initBean(Class<T> t, Map instanceMap, ApplicationContext applicationContext) throws BeansException {

        Map<String, T> beanMap = applicationContext.getBeansOfType(t);

        beanMap.forEach((key, instance) -> {
            instanceMap.put(instance.getServiceName().toUpperCase() + "_" + instance.getVersion().toUpperCase(), instance);
        });
    }
}
