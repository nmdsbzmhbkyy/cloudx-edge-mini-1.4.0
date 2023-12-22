package com.aurine.cloudx.estate.openapi.sync.factory;

import com.aurine.cloudx.estate.openapi.sync.service.SyncService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：zouyu
 * @data: 2022/3/22 18:48
 */
@Component
public class SyncFactoryProducer implements ApplicationContextAware {

    private static Map<String, SyncService> instanceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, SyncService> map = applicationContext.getBeansOfType(SyncService.class);
        instanceMap = new HashMap<>();
        map.forEach((key, value) -> instanceMap.put(value.getVersion(), value));
    }


    /**
     * 根据名称获实例
     *
     * @param name
     * @return
     */
    public static SyncService getInstance(String name) {
        if (instanceMap.get(name) == null) {
            throw new RuntimeException("未找到 SyncService 实例:" + name);
        }
        return instanceMap.get(name);
    }

}
