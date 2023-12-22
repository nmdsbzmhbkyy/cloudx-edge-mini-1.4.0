package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.AurineEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.util.AurineEdgeUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.AurineEdgeRemoteDeviceGroupService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: AurineEdgeRemoteDeviceGroupServiceImplV1
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午02:50:25
 * @Copyright:
 */
@Service
@Slf4j
public class AurineEdgeRemoteDeviceGroupServiceImplV1 implements AurineEdgeRemoteDeviceGroupService {

    @Resource
    AurineEdgeDataConnector aurineEdgeDataConnector;

    @Override
    public AurineEdgeRespondDTO addDeviceGroup(AurineEdgeConfigDTO configDTO, String groupName) {
        /*String uri = "device_group/add";
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupName", "S" + groupName);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, this.getVersion());
        JSONObject send = aurineEdgeDataConnector.send(requestObject);
        return AurineEdgeDeviceDataUtil.handelRespond(send);*/
        return null;
    }

    @Override
    public AurineEdgeRespondDTO delDeviceGroup(AurineEdgeConfigDTO configDTO, String groupId) {
        /*String uri = "device_group/" + groupId + "/del";
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());

        JSONObject resultJson = aurineEdgeDataConnector.send(requestObject);
        log.info("[华为中台] 删除设备组 获取返回值：{}", resultJson);

        return AurineEdgeDeviceDataUtil.handelRespond(resultJson);*/
        return null;
    }

    @Override
    public AurineEdgeRespondDTO queryDeviceGroup(AurineEdgeConfigDTO configDTO, String groupId, Integer pageNo, Integer pageSize, String marker) {
        /*String uri = "device_group/" + groupId + "/device/query?pageNo=" + pageNo.toString() + "&pageSize=" + pageSize.toString() + "&marker=" + marker;
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));*/
        return null;
    }

    @Override
    public AurineEdgeRespondDTO deviceGroupManager(AurineEdgeConfigDTO configDTO, String groupId, List<String> deviceIdList, String active) {
        /*String uri = "device_group/device/manager";
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupId", groupId);
        requestJson.put("deviceIds", deviceIdList);
        requestJson.put("active", active);
        AurineEdgeRequestObject postRequestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(postRequestObject));*/
        return null;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
