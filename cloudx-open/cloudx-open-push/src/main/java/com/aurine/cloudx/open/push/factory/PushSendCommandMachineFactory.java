package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.push.machine.PushSendCommandMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 推送发送指令业务类工厂
 *
 * @author : Qiu
 * @date : 2021 12 13 16:28
 */

@Component
public class PushSendCommandMachineFactory extends AbstractPushSendMachineFactory implements ApplicationContextAware {

    private static PushSendCommandMachineFactory instance;

    private Map<String, PushSendCommandMachine> machineMap;

    private PushSendCommandMachineFactory() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PushSendCommandMachineFactory.instance = applicationContext.getBean(PushSendCommandMachineFactory.class);
        machineMap = initServiceMap(PushSendCommandMachine.class, applicationContext);
    }

    public PushSendCommandMachine getMachine(String serviceName) {
        return getMachine(serviceName, null);
    }

    public PushSendCommandMachine getMachine(String serviceName, String version) {
        if (StringUtils.isNotEmpty(serviceName)) {
            String key = buildKey(serviceName, version);
            if (!machineMap.containsKey(key)) return null;
            return machineMap.get(key);
        } else {
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_TYPE_INVALID);
        }
    }

    public static PushSendCommandMachineFactory getInstance() {
        return instance;
    }
}
