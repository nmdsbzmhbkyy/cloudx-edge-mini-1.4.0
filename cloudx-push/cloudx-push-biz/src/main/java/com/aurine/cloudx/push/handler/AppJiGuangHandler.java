package com.aurine.cloudx.push.handler;

import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.push.config.PushAppJiGuangConfig;
import com.aurine.cloudx.push.entity.PushMessage;
import com.cloudx.common.push.util.PushAppJGuangUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 极光 推送信息 处理器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */
@Component("JI_GUANG")
public class AppJiGuangHandler implements AppHandlerStrategy {

    @Autowired
    private PushAppJiGuangConfig pushAppJiGuangConfig;

    @Override
    public boolean sendMsgToApp(PushMessage pushMessage) {

        List<String> idList = new ArrayList<>();
        idList.add(pushMessage.getTargetId());

        Map<String,String> extras = new HashMap<>();

        //根据APP名称获取参数
        Map<String, String> configMap = getConfigByName(pushAppJiGuangConfig.getAppList(), pushMessage.getAppName());
        String appKey = MapUtil.getStr(configMap, "appKey");
        String masterSecret = MapUtil.getStr(configMap, "masterSecret");
        Integer deploy = MapUtil.getInt(configMap, "deploy");


        // 调用推送
        return PushAppJGuangUtil.JGuangPush(pushMessage.getMessage(),idList,extras,deploy,masterSecret, appKey);

    }

    /**
     * 群推给APP
     *
     * @param pushMessage
     * @return
     */
    @Override
    public boolean batchSendMsgToApps(PushMessage pushMessage) {

        Map<String,String> extras = new HashMap<>();

        //根据APP名称获取参数
        Map<String, String> configMap = getConfigByName(pushAppJiGuangConfig.getAppList(), pushMessage.getAppName());
        String appKey = MapUtil.getStr(configMap, "appKey");
        String masterSecret = MapUtil.getStr(configMap, "masterSecret");
        Integer deploy = MapUtil.getInt(configMap, "deploy");


        // 调用推送
        return PushAppJGuangUtil.JGuangPush(pushMessage.getMessage(),pushMessage.getTargetIdList(),extras,deploy,masterSecret, appKey);
    }
}
