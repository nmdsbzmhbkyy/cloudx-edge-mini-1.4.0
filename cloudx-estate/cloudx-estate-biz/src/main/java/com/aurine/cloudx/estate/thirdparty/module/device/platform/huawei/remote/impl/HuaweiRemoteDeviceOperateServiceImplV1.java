package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.HuaweiDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.message.InfoObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.HuaweiRemoteDeviceOperateService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiDeviceDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @ClassName: HuaweiRemoteDeviceOperateServiceImplV2
 * @author: 王良俊 <>
 * @date: 2020年11月24日 下午04:03:53
 * @Copyright:
 */
@Service
@Slf4j
public class HuaweiRemoteDeviceOperateServiceImplV1 implements HuaweiRemoteDeviceOperateService {

    @Resource
    HuaweiDataConnector huaweiDataConnector;

    /**
     * <p>
     * 下发设备命令异步
     * 例如：如下开门
     * {
     * "devId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
     * "msgId": "12312312",
     * "commandInfo": {
     * "serviceId": "DeviceControl",
     * "commandName": "OPENDOOR",
     * "params": {
     * }
     * }
     * }
     * </p>
     *
     * @param
     */
    @Override
    public HuaweiRespondDTO commandsDown(HuaweiConfigDTO configDTO, String deviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams) {
        //1、init
        if (paramsJson == null) {
            paramsJson = new JSONObject();
        }

        //生成请求对象
        String uri = "device/" + deviceId + "/commands/down";

        JSONObject commandInfoJson = new JSONObject();
        commandInfoJson.put("serviceId", serviceId);
        commandInfoJson.put("commandName", commandName);
        commandInfoJson.put("params", paramsJson);

        JSONObject requestJson = new JSONObject();
        requestJson.put("devId", deviceId);
        requestJson.put("commandInfo", commandInfoJson);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * <p>
     * 下发命令同步
     * </p>
     */
    @Override
    public HuaweiRespondDTO syncCommandsDown(HuaweiConfigDTO configDTO, String productId, String deviceId, String serviceId, String commandName, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + deviceId + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfoJson = new JSONObject();

        commandInfoJson.put("serviceId", serviceId);
        commandInfoJson.put("commandName", commandName);

        requestJson.put("devId", deviceId);
        requestJson.put("commandInfo", commandInfoJson);
        requestJson.put("productId", productId);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));

    }

    /**
     * <p>
     * <p>
     * {
     * "devId": "${deviceCode}",
     * "properties": [
     * {
     * "serviceId": "DeviceParam",
     * "propertieTag": "netparam",
     * "value": {
     * "ipAddr": "192.168.1.248",
     * "netmask": "255.255.255.0",
     * "gateway": "192.168.1.1",
     * "dns": "058.022.096.066"
     * }
     * }
     * ]
     * }
     * </p>
     */
    @Override
    public HuaweiRespondDTO propertiesDown(HuaweiConfigDTO configDTO, String deviceId, String productId, JSONArray properties, JSONObject otherParams) {
        JSONObject requestJson = new JSONObject();
        String uri = "device/" + deviceId + "/properties/down";
        requestJson.put("productId", productId);
        requestJson.put("devId", deviceId);
        requestJson.put("properties", properties);
        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }


    @Override
    public HuaweiRespondDTO messageDown(HuaweiConfigDTO configDTO, String thirdDeviceId, String productId, JSONObject messageInfo, JSONObject otherParams) {
        JSONObject requestJson = new JSONObject();
        String uri = "device/" + thirdDeviceId + "/message/down";
        requestJson.put("devId", thirdDeviceId);
        requestJson.put("productId", productId);
        requestJson.put("messageInfo", messageInfo);

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * @return
     * @throws
     */
    @Override
    public HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams) {
        return this.objectManage(configDTO, null, thirdDeviceId, serviceId, action, objName, params, otherParams);
    }

    /**
     * 子设备指令统一方法
     *
     * @param configDTO
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    @Override
    public HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams) {
        return this.objectManage(configDTO, null, thirdDeviceId, serviceId, action, objName, params, otherParams);
    }

    /**
     * 子设备指令统一方法
     *
     * @param configDTO
     * @param msgId         自定义消息ID
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    @Override
    public HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("serviceId", serviceId);
        actionInfo.put("action", action);
        actionInfo.put("objName", objName);
        actionInfo.put("params", params);

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);

        if (StringUtils.isNotEmpty(msgId)) {
            requestJson.put("msgId", msgId);
        }

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    /**
     * 子设备指令统一方法
     *
     * @param configDTO
     * @param msgId         自定义消息ID
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    @Override
    public HuaweiRespondDTO objectManageSync(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("serviceId", serviceId);
        actionInfo.put("action", action);
        actionInfo.put("objName", objName);
        actionInfo.put("params", params);

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);

        if (StringUtils.isNotEmpty(msgId)) {
            requestJson.put("msgId", msgId);
        }


        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(),otherParams);
        String reqKey = "HUAWEI_SYNC_REQ_" + requestObject.getMessageId();
        String respKey = "HUAWEI_SYNC_RESP_" + requestObject.getMessageId();

        JSONObject respJson = huaweiDataConnector.sendSync(requestObject, reqKey, respKey);
        HuaweiRespondDTO respondDTO = new HuaweiRespondDTO();
        respondDTO.setBodyObj(respJson);
        respondDTO.setErrorEnum(HuaweiErrorEnum.SUCCESS);

//        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.sendSync(requestObject, reqKey, respKey));
        return respondDTO;
    }

    /**
     * 子设备指令统一方法
     *
     * @param configDTO
     * @param msgId         自定义消息ID
     * @param thirdDeviceId 设备ID
     * @param serviceId     服务ID
     * @param action        行为
     * @param objName       操作对象名
     * @param params        数据对象
     * @param otherParams   其他参数
     * @return
     */
    @Override
    public HuaweiRespondDTO objectManage(HuaweiConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId,
                                         String action, String objName, JSONObject params, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("serviceId", serviceId);
        actionInfo.put("action", action);
        actionInfo.put("objName", objName);
        actionInfo.put("params", params);

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);

        if (StringUtils.isNotEmpty(msgId)) {
            requestJson.put("msgId", msgId);
        }


        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(), otherParams);

        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    @Override
    public HuaweiRespondDTO sendMessage(HuaweiConfigDTO config, InfoObj infoObj, String thirdDeviceId) {
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = this.getClassManagerRequestJson("ADD", infoObj, thirdDeviceId, InfoObj.getObjName(), UUID.randomUUID().toString().replaceAll("-", ""));

        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(config, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    private JSONObject getClassManagerRequestJson(String action, Object objInfo, String thirdDeviceId, String objName, String msgId) {
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("action", action);
        actionInfo.put("params", objInfo);
        actionInfo.put("objName", objName);
        actionInfo.put("msgId", msgId);
        actionInfo.put("serviceId", "ClassManager");

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);
        requestJson.put("msgId", msgId);
        return requestJson;
    }

    @Override
    public HuaweiRespondDTO cleanMessage(HuaweiConfigDTO config, String thirdDeviceId) {
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = this.getClassManagerRequestJson("CLEAN", new JSONObject(), thirdDeviceId, InfoObj.getObjName(), UUID.randomUUID().toString().replaceAll("-", ""));
        HuaweiRequestObject requestObject = HuaweiUtil.createPostRequestObject(config, uri, requestJson, getVersion());
        return HuaweiDeviceDataUtil.handelRespond(huaweiDataConnector.send(requestObject));
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

}
