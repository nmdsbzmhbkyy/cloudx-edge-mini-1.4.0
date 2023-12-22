package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.push.machine.PushBaseMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送发送业务工厂抽象类
 *
 * @author : Qiu
 * @date : 2022 01 28 11:53
 */

public abstract class AbstractPushSendMachineFactory {

    static <T extends PushBaseMachine> Map<String, T> initServiceMap(Class<T> t, ApplicationContext applicationContext) {
        Map<String, T> beanMap = applicationContext.getBeansOfType(t);
        Map<String, T> machineMap = new HashMap<>();

        beanMap.forEach((key, machine) -> {
            machineMap.put(buildKey(machine), machine);
        });

        return machineMap;
    }

    static String buildKey(PushBaseMachine service) {
        return buildKey(service.getServiceName(), service.getVersion());
    }

    static String buildKey(String serviceName, String version) {
        String key = serviceName.toUpperCase();
        if (StringUtils.isNotEmpty(version)) key += "_" + version.toUpperCase();

        return key;
    }
}
