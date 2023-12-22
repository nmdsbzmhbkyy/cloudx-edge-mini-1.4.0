package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.HuaweiDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.HuaweiRemoteSubscriptionsService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiDeviceDataUtil;
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
public class HuaweiRemoteSubscriptionsServiceImplV1 implements HuaweiRemoteSubscriptionsService {

    @Resource
    HuaweiDataConnector huaweiDataConnector;

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    @Override
    public HuaweiRespondDTO addSubscriptions(HuaweiConfigDTO configDTO, String callbackurl, String channel, String resource, String event) {
        String uri = "sys/subscriptions/add";

        JSONObject requestJson = new JSONObject();
        requestJson.put("callbackurl", callbackurl);
        requestJson.put("channel", channel);

        JSONObject subjectJson = new JSONObject();
        subjectJson.put("resource", resource);
        subjectJson.put("event", event);

        requestJson.put("subject", subjectJson);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    @Override
    public HuaweiRespondDTO updateSubscriptions(HuaweiConfigDTO configDTO, String subscriptionId, String callbackurl, String channel) {
        String uri = "sys/subscriptions/" + subscriptionId + "/update";
        JSONObject requestJson = new JSONObject();
        requestJson.put("callbackurl", callbackurl);
        requestJson.put("channel", channel);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    @Override
    public HuaweiRespondDTO delSubscriptions(HuaweiConfigDTO configDTO, String subscriptionId) {
        String uri = "sys/subscriptions/" + subscriptionId + "/del";
        HuaweiRequestObject getRequestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(getRequestObject));
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
