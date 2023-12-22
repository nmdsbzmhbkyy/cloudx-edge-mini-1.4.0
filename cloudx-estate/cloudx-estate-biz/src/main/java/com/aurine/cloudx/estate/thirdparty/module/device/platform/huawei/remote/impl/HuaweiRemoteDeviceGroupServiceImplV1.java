package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.HuaweiDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.HuaweiRemoteDeviceGroupService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiDeviceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: HuaweiRemoteDeviceGroupServiceImplV2
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午02:50:25
 * @Copyright:
 */
@Service
@Slf4j
public class HuaweiRemoteDeviceGroupServiceImplV1 implements HuaweiRemoteDeviceGroupService {

    @Resource
    HuaweiDataConnector huaweiDataConnector;

    @Override
    public HuaweiRespondDTO addDeviceGroup(HuaweiConfigDTO configDTO, String groupName) {
        /*String uri = "device_group/add";
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupName", "S" + groupName);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, this.getVersion());
        JSONObject send = huaweiDataConnector.send(requestObject);
        return HuaweiDeviceDataUtil.handelRespond(send);*/
        return null;
    }

    @Override
    public HuaweiRespondDTO delDeviceGroup(HuaweiConfigDTO configDTO, String groupId) {
        /*String uri = "device_group/" + groupId + "/del";
        HuaweiRequestObject requestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());

        JSONObject resultJson = huaweiDataConnector.send(requestObject);
        log.info("[华为中台] 删除设备组 获取返回值：{}", resultJson);

        return HuaweiDeviceDataUtil.handelRespond(resultJson);*/
        return null;
    }

    @Override
    public HuaweiRespondDTO queryDeviceGroup(HuaweiConfigDTO configDTO, String groupId, Integer pageNo, Integer pageSize, String marker) {
        /*String uri = "device_group/" + groupId + "/device/query?pageNo=" + pageNo.toString() + "&pageSize=" + pageSize.toString() + "&marker=" + marker;
        HuaweiRequestObject requestObject = HuaweiUtil.createGetRequestObject(configDTO, uri, new JSONObject(), getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));*/
        return null;
    }

    @Override
    public HuaweiRespondDTO deviceGroupManager(HuaweiConfigDTO configDTO, String groupId, List<String> deviceIdList, String active) {
        /*String uri = "device_group/device/manager";
        JSONObject requestJson = new JSONObject();
        requestJson.put("groupId", groupId);
        requestJson.put("deviceIds", deviceIdList);
        requestJson.put("active", active);
        HuaweiRequestObject postRequestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(postRequestObject));*/
        return null;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
