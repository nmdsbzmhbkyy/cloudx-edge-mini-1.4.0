package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.config.YushiSubscribeConfig;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.core.YushiDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request.YushiConnectDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.respond.YushiResponse;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.enums.YushiApiEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.remote.YushiRemoteDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 对接实现，用于拼接对接参数等逻辑
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Service
@Slf4j
public class YushiRemoteDeviceServiceImplV1 implements YushiRemoteDeviceService {
    @Resource
    YushiDataConnector YushiDataConnector;
    @Resource
    private YushiSubscribeConfig subscribeConfig;


    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    @Override
    public JSONObject systemDeviceInfo(YushiConnectDTO yushiConnectDTO) throws Exception {
        try {
            return YushiDataConnector.send(yushiConnectDTO, YushiApiEnum.SYSTEM_DEVICE_INFO.getApi(), null, YushiApiEnum.SYSTEM_DEVICE_INFO.getMethod());
        } catch (Exception e) {
            throw new RuntimeException("获取设备信息失败！");
        }
    }

    @Override
    public JSONObject eventSubscribe(YushiConnectDTO yushiConnectDTO) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AddressType", 0);
        jsonObject.put("IPAddress", subscribeConfig.getIp());
        jsonObject.put("Port", subscribeConfig.getPort());
        jsonObject.put("Duration", 3600);

        try {
            return YushiDataConnector.send(yushiConnectDTO, YushiApiEnum.EVENT_SUBSCRIPTION.getApi(), jsonObject, YushiApiEnum.EVENT_SUBSCRIPTION.getMethod());
        } catch (Exception e) {
            throw new RuntimeException("设备事件订阅失败！");
        }
    }

    @Override
    public JSONObject eventSubscribeRefresh(YushiConnectDTO yushiConnectDTO, Integer subscribeId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Duration", 3600);

        String url = YushiApiEnum.EVENT_SUBSCRIPTION_REFRESH.getApi() + "/" + subscribeId;
        try {
            return YushiDataConnector.send(yushiConnectDTO, url, jsonObject, YushiApiEnum.EVENT_SUBSCRIPTION_REFRESH.getMethod());
        } catch (Exception e) {
            throw new RuntimeException("设备刷新事件订阅失败！");
        }
    }

    @Override
    public JSONObject videoLiveStream(YushiConnectDTO yushiConnectDTO) throws Exception {
        try {
            return YushiDataConnector.send(yushiConnectDTO, YushiApiEnum.VIDEO_LIVE_STREAM.getApi(), null, YushiApiEnum.VIDEO_LIVE_STREAM.getMethod());
        } catch (Exception e) {
            throw new RuntimeException("获取视频流失败！");
        }
    }
}
