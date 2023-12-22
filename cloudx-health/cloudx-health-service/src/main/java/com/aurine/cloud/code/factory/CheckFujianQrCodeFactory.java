package com.aurine.cloud.code.factory;


import com.aurine.cloud.code.entity.enums.VersionEnum;
import com.aurine.cloud.code.platform.PassWayHealthService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 区分不同地区健康码工厂
 *
 * @ClassName: AbstractHealthFactory
 * @author: yz <wangwei@aurine.cn>
 */
@Component
public class CheckFujianQrCodeFactory extends AbstractHealthFactory implements ApplicationContextAware {


    private static CheckFujianQrCodeFactory checkFujianQrCodeFactory = null;
    private static Map<String, PassWayHealthService> passWayHealthServiceInstanceMap = new HashMap<>();
    private Map<String, PassWayHealthService> passWayHealthServiceMap = new HashMap<>();

    private CheckFujianQrCodeFactory() {
    }

    public static CheckFujianQrCodeFactory getInstance() {
        if (checkFujianQrCodeFactory == null) {
            synchronized (CheckFujianQrCodeFactory.class) {
                if (checkFujianQrCodeFactory == null) {
                    checkFujianQrCodeFactory = new CheckFujianQrCodeFactory();
                }
            }
        }
        return checkFujianQrCodeFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        passWayHealthServiceInstanceMap = applicationContext.getBeansOfType(PassWayHealthService.class);
    }


    @Override
    public PassWayHealthService getPassWayHealthService(String versionNumberPlaceName) {


        if (passWayHealthServiceMap.get(versionNumberPlaceName)==null) {
            passWayHealthServiceInstanceMap.forEach((key, instance) -> {
                if (VersionEnum.getByplaceName(versionNumberPlaceName).code.equals(instance.getVersion())) {
                    passWayHealthServiceMap.put(versionNumberPlaceName, instance);
                }
            });
            if (passWayHealthServiceMap.size() == 0) {
                throw new RuntimeException("未找到该版本实例:" + versionNumberPlaceName);
            }
        }
        return passWayHealthServiceMap.get(versionNumberPlaceName);
    }


}
