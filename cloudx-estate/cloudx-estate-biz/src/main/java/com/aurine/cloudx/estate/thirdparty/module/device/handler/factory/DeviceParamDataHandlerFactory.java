package com.aurine.cloudx.estate.thirdparty.module.device.handler.factory;

import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.base.BaseParamDataHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>参数数据转换类工厂</p>
 *
 * @author 王良俊
 * @date "2022/3/4"
 */
@Component
public class DeviceParamDataHandlerFactory implements ApplicationContextAware {

    private static Map<String, BaseParamDataHandler> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BaseParamDataHandler> beansOfType = applicationContext.getBeansOfType(BaseParamDataHandler.class);
        Collection<BaseParamDataHandler> values = beansOfType.values();
        values.forEach(handle -> {
            beanMap.put(handle.getServiceId().serviceId + handle.getPlateForm().code, handle);
        });
    }

    public static List<BaseParamDataHandler> getService(List<String> serviceIdList, PlatformEnum plateForm) {
        List<BaseParamDataHandler> handleList = new ArrayList<>();
        for (String serviceId : serviceIdList) {
            BaseParamDataHandler abstractParamDataHandle = beanMap.get(serviceId + plateForm.code);
            if (abstractParamDataHandle != null) {
                handleList.add(abstractParamDataHandle);
            }
        }
        return handleList;
    }

}
