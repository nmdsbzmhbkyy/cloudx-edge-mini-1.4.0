package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.factory;

import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.base.AbstractExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>异常处理类工厂</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 10:58:26
 */
@Component
public class AbnormalHandleFactory implements ApplicationContextAware {

    /**
     * 处理类bean容器
     */
    private static final Map<String, AbstractExceptionHandler> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AbstractExceptionHandler> beansOfType = applicationContext.getBeansOfType(AbstractExceptionHandler.class);
        if (MapUtil.isNotEmpty(beansOfType)) {
            Collection<AbstractExceptionHandler> values = beansOfType.values();
            values.forEach(handler -> {
                beanMap.put(handler.getTargetParam().code, handler);
            });
        }
    }

    /**
     * <p>获取某个handler</p>
     *
     * @param paramEnum 处理类所负责的参数
     * @author: 王良俊
     */
    public static AbstractExceptionHandler getHandler(DeviceRegParamEnum paramEnum) {
        return beanMap.get(paramEnum.code);
    }

    /**
     * <p>获取多个handler</p>
     *
     * @param paramEnumList 处理类所负责的参数们
     * @author: 王良俊
     */
    public static List<AbstractExceptionHandler> getHandlerList(Set<DeviceRegParamEnum> paramEnumList) {
        List<AbstractExceptionHandler> handlerList = new ArrayList<>();
        paramEnumList.forEach(paramEnum -> {
            if (beanMap.get(paramEnum.code) != null) {
                handlerList.add(beanMap.get(paramEnum.code));
            }
        });
        return handlerList;
    }

}
