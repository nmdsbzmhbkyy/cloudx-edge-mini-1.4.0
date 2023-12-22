package com.aurine.cloudx.estate.excel.invoke.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>设备导入解析服务工具类</p>
 * @author : 王良俊
 * @date : 2021-09-02 17:09:51
 */
@Component
public class ExcelInvokeServiceFactory implements ApplicationContextAware {

    private static final Map<String, BaseExcelRowInvokeService> beanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BaseExcelRowInvokeService> beansOfType = applicationContext.getBeansOfType(BaseExcelRowInvokeService.class);
        Collection<BaseExcelRowInvokeService> values = beansOfType.values();
        values.forEach(service -> {
            Set<String> deviceTypeSet = service.getDeviceTypeSet();
            if (CollectionUtil.isNotEmpty(deviceTypeSet)) {
                deviceTypeSet.forEach(deviceType -> {
                    beanMap.put(deviceType, service);
                });
            }
        });
    }

    /**
    * <p>
    * 根据设备类ID获取服务
    * </p>
    *
    * @param deviceType 设备类型ID
    */
    public static BaseExcelRowInvokeService getInstance(String deviceType) {
        return beanMap.get(deviceType);
    }

}
