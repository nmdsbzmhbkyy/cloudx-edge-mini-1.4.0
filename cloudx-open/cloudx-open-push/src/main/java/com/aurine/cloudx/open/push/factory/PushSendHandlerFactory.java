package com.aurine.cloudx.open.push.factory;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.push.handler.PushSendHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送发送处理类工厂
 *
 * @author : Qiu
 * @date : 2021 12 13 16:28
 */

@Component
public class PushSendHandlerFactory implements ApplicationContextAware {

    private static final Map<String, PushSendHandler> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PushSendHandler> beansOfType = applicationContext.getBeansOfType(PushSendHandler.class);
        beansOfType.forEach((key, instance) -> {
            beanMap.put(buildKey(instance), instance);
        });
    }

    public static PushSendHandler getHandler(OpenPushModel model) {
        return getHandler(model, null);
    }

    public static PushSendHandler getHandler(OpenPushModel model, String version) {
        String serviceType = model.getServiceType();

        return getHandler(serviceType, version);
    }

    public static PushSendHandler getHandler(String serviceType, String version) {
        if (StringUtils.isNotEmpty(serviceType)) {
            String key = buildKey(serviceType, version);
            if (!beanMap.containsKey(key)) throw new CloudxOpenException(CloudxOpenErrorEnum.NOT_FOUND);
            return beanMap.get(key);
        } else {
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_TYPE_INVALID);
        }
    }

    private static String buildKey(PushSendHandler pushSendHandler) {
        return buildKey(pushSendHandler.getServiceType(), pushSendHandler.getVersion());
    }

    private static String buildKey(String serviceType, String version) {
        String key = serviceType.toUpperCase();
        if (StringUtils.isNotEmpty(version)) key += "_" + version.toUpperCase();

        return key;
    }
}
