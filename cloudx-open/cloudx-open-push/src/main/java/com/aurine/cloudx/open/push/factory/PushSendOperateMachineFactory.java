package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.push.machine.PushSendOperateMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 推送发送操作业务类工厂
 *
 * @author : Qiu
 * @date : 2021 12 13 16:28
 */

@Component
public class PushSendOperateMachineFactory extends AbstractPushSendMachineFactory implements ApplicationContextAware {

    private static PushSendOperateMachineFactory instance;

    private Map<String, PushSendOperateMachine> machineMap;

    private PushSendOperateMachineFactory() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PushSendOperateMachineFactory.instance = applicationContext.getBean(PushSendOperateMachineFactory.class);
        machineMap = initServiceMap(PushSendOperateMachine.class, applicationContext);
    }

    public PushSendOperateMachine getMachine(String serviceName) {
        return getMachine(serviceName, null);
    }

    public PushSendOperateMachine getMachine(String serviceName, String version) {
        if (StringUtils.isNotEmpty(serviceName)) {
            String key = buildKey(serviceName, version);
            if (!machineMap.containsKey(key)) return null;
            return machineMap.get(key);
        } else {
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_TYPE_INVALID);
        }
    }

    public static PushSendOperateMachineFactory getInstance() {
        return instance;
    }
}
