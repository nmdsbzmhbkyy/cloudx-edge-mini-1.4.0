package com.aurine.cloudx.estate.subscribe.factory;


import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 用于订阅的简单工厂
 *
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-28 17:20
 */
@Service
public class EventFactory {

    @Autowired
    Map<String, EventSubscribeService> instanceMap = new ConcurrentHashMap<>(3);



    public EventSubscribeService GetProduct(String type) {
        EventSubscribeService eventSubscribeService = instanceMap.get(type);
        if (eventSubscribeService == null) {
            throw new RuntimeException("未获取到对应的工厂");
        }
        return eventSubscribeService;
    }
}
