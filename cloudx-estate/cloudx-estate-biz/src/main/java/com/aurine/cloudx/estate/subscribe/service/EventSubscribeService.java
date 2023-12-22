package com.aurine.cloudx.estate.subscribe.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-28 17:18
 */
public interface EventSubscribeService {
    /**
     * 发送订阅消息的处理类
     *
     * @param jsonObject
     * @param projectId
     * @param type
     * @return
     */
    Boolean send(JSONObject jsonObject, Integer projectId, String type);
}
