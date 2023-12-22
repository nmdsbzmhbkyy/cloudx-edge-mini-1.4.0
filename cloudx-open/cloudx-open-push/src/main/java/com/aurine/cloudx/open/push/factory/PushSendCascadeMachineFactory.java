package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.push.machine.PushSendCascadeMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 推送发送级联入云业务类工厂
 *
 * @author : Qiu
 * @date : 2021 12 27 16:28
 */

@Component
public class PushSendCascadeMachineFactory extends AbstractPushSendMachineFactory implements ApplicationContextAware {

    private static PushSendCascadeMachineFactory instance;

    private Map<String, PushSendCascadeMachine> machineMap;

    private PushSendCascadeMachineFactory() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PushSendCascadeMachineFactory.instance = applicationContext.getBean(PushSendCascadeMachineFactory.class);
        this.machineMap = initServiceMap(PushSendCascadeMachine.class, applicationContext);
    }

    public PushSendCascadeMachine getMachine(String serviceName) {
        return getMachine(serviceName, null);
    }

    public PushSendCascadeMachine getMachine(String serviceName, String version) {
        if (StringUtils.isNotEmpty(serviceName)) {
            String key = buildKey(serviceName, version);
            if (!machineMap.containsKey(key)) return null;
            return machineMap.get(key);
        } else {
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_TYPE_INVALID);
        }
    }

    public static PushSendCascadeMachineFactory getInstance() {
        return instance;
    }
}
