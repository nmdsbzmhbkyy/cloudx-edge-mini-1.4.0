package com.aurine.cloudx.push.handler;

import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.push.config.PushAppGeTuiConfig;
import com.aurine.cloudx.push.entity.PushMessage;
import com.cloudx.common.push.constant.OSTypeEnum;
import com.cloudx.common.push.util.PushAppGTuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 个推 推送信息 处理器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */
@Component("GE_TUI")
public class AppGeTuiHandler implements AppHandlerStrategy {

    @Autowired
    private PushAppGeTuiConfig pushAppGeTuiConfig;

    @Override
    public boolean sendMsgToApp(PushMessage pushMessage) {

        //获取配置

        //根据配置参数
        Map<String, String> customsMsp = MapUtil.get(pushMessage.getParamMap(), "cunstoms", Map.class);
        String intent = MapUtil.getStr(pushMessage.getParamMap(), "intent");
        String serverUrl = pushAppGeTuiConfig.getUrl();

        //根据APP名称获取参数
        Map<String, String> configMap = getConfigByName(pushAppGeTuiConfig.getAppList(), pushMessage.getAppName());

        String appId = MapUtil.getStr(configMap, "appId");
        String appKey = MapUtil.getStr(configMap, "appKey");
        String masterSecret = MapUtil.getStr(configMap, "masterSecret");

        //推送
        return PushAppGTuiUtil.GTuiPush(pushMessage.getMessage(), customsMsp, pushMessage.getTargetId(), pushMessage.getOsType(), serverUrl, appId, appKey, masterSecret, intent);
    }

    /**
     * 群推给APP
     *
     * @param pushMessage
     * @return
     */
    @Override
    public boolean batchSendMsgToApps(PushMessage pushMessage) {
        //获取配置

        //根据配置参数
        Map<String, String> customsMsp = MapUtil.get(pushMessage.getParamMap(), "cunstoms", Map.class);
        String intent = MapUtil.getStr(pushMessage.getParamMap(), "intent");
        String serverUrl = pushAppGeTuiConfig.getUrl();

        //封装群发客户端列表
        List<Object[]> infos = new ArrayList<>();
        Object[] objArray = null;

        for (int i = 0; i < pushMessage.getTargetIdList().size(); i++) {
            objArray = new Object[3];
            objArray[0] = "";
            objArray[1] = pushMessage.getOsTypeList().get(i) == OSTypeEnum.android ? 3 : 4;
            objArray[2] = pushMessage.getTargetIdList().get(i);
            infos.add(objArray);
        }


        //根据APP名称获取参数
        Map<String, String> configMap = getConfigByName(pushAppGeTuiConfig.getAppList(), pushMessage.getAppName());

        String appId = MapUtil.getStr(configMap, "appId");
        String appKey = MapUtil.getStr(configMap, "appKey");
        String masterSecret = MapUtil.getStr(configMap, "masterSecret");


        return PushAppGTuiUtil.GTuiBatchPush(pushMessage.getMessage(), customsMsp, infos, serverUrl, appId, appKey, masterSecret, intent);
    }
}
