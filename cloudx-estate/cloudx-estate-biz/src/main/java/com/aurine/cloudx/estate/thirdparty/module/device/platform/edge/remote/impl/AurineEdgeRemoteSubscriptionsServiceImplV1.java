package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.AurineEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.util.AurineEdgeUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.AurineEdgeRemoteSubscriptionsService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName: HuaweiRemoteSubscriptionsServiceImplV2
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午02:57:44
 * @Copyright:
 */
@Service
@Slf4j
public class AurineEdgeRemoteSubscriptionsServiceImplV1 implements AurineEdgeRemoteSubscriptionsService {

    @Resource
    AurineEdgeDataConnector aurineEdgeDataConnector;

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    @Override
    public AurineEdgeRespondDTO addSubscriptions(AurineEdgeConfigDTO configDTO, String callbackurl, String channel, String resource, String event) {
        String uri = "sys/subscriptions/add";

        JSONObject requestJson = new JSONObject();
        requestJson.put("callbackurl", callbackurl);
        requestJson.put("channel", channel);

        JSONObject subjectJson = new JSONObject();
        subjectJson.put("resource", resource);
        subjectJson.put("event", event);

        requestJson.put("subject", subjectJson);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO updateSubscriptions(AurineEdgeConfigDTO configDTO, String subscriptionId, String callbackurl, String channel) {
        String uri = "sys/subscriptions/" + subscriptionId + "/update";
        JSONObject requestJson = new JSONObject();
        requestJson.put("callbackurl", callbackurl);
        requestJson.put("channel", channel);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO delSubscriptions(AurineEdgeConfigDTO configDTO, String subscriptionId) {
        String uri = "sys/subscriptions/" + subscriptionId + "/del";
        AurineEdgeRequestObject getRequestObject = AurineEdgeUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(getRequestObject));
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
