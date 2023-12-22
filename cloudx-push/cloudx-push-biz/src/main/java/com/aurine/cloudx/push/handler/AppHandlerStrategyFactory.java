package com.aurine.cloudx.push.handler;

import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.aurine.cloudx.push.entity.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: App推送策略工厂
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */

@Service
public class AppHandlerStrategyFactory {

    @Autowired
    private final Map<String, AppHandlerStrategy> strategyMap = new ConcurrentHashMap<>();

    public AppHandlerStrategyFactory(Map<String, AppHandlerStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }

    /**
     * 获取Handler实例
     * @param appSystemType
     * @return
     */
    public AppHandlerStrategy getInstance (PushSystemEnum appSystemType){
        return strategyMap.get(appSystemType.name());
    }

    public boolean sendMsgToApp(PushMessage pushMessage){
        return strategyMap.get(pushMessage.getAppPushSystem().name()).sendMsgToApp(pushMessage);
    }

    public boolean batchSendMsgToApps(PushMessage pushMessage){
        return strategyMap.get(pushMessage.getAppPushSystem().name()).batchSendMsgToApps(pushMessage);
    }
}
