package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.push.machine.PushSendOtherMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 推送发送其他业务类工厂
 *
 * @author : Qiu
 * @date : 2021 12 13 16:28
 */

@Component
public class PushSendOtherMachineFactory extends AbstractPushSendMachineFactory implements ApplicationContextAware {

    private static PushSendOtherMachineFactory instance;

    private Map<String, PushSendOtherMachine> machineMap;

    private PushSendOtherMachineFactory() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PushSendOtherMachineFactory.instance = applicationContext.getBean(PushSendOtherMachineFactory.class);
        machineMap = initServiceMap(PushSendOtherMachine.class, applicationContext);
    }

    public PushSendOtherMachine getMachine(String serviceName) {
        return getMachine(serviceName, null);
    }

    public PushSendOtherMachine getMachine(String serviceName, String version) {
        if (StringUtils.isNotEmpty(serviceName)) {
            String key = buildKey(serviceName, version);
            if (!machineMap.containsKey(key)) return null;
            return machineMap.get(key);
        } else {
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_TYPE_INVALID);
        }
    }

    public static PushSendOtherMachineFactory getInstance() {
        return instance;
    }
}
