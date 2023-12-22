package com.aurine.cloudx.push.handler;

import com.aurine.cloudx.push.entity.PushMessage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 发送信息给APP 策略接口
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */
public interface AppHandlerStrategy {
    /**
     * 单推给APP
     * @param pushMessage
     * @return
     */
    boolean sendMsgToApp(PushMessage pushMessage);

    /**
     * 群推给APP
     * @param pushMessage
     * @return
     */
    boolean batchSendMsgToApps(PushMessage pushMessage);

    /**
     * 根据APP内部名称获取对应配置
     * @param name
     * @return
     */
    default Map<String,String> getConfigByName(List<Map<String,String>> paramsList, String name){
        if(paramsList == null || paramsList.size() == 0) return null;

       List<Map<String,String>> resultList = paramsList.stream().filter(map -> map.get("name").equalsIgnoreCase(name)).collect(Collectors.toList());
       return resultList == null || resultList.size() ==0? null:resultList.get(0);
    }
}
