package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.AurineEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.util.AurineEdgeUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeCommandConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.message.NoticeMessageInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.AurineEdgeRemoteDeviceOperateService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.message.InfoObj;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: HuaweiRemoteDeviceOperateServiceImplV2
 * @author: 王良俊 <>
 * @date: 2020年11月24日 下午04:03:53
 * @Copyright:
 */
@Service
@Slf4j
public class AurineEdgeRemoteDeviceOperateServiceImplV1 implements AurineEdgeRemoteDeviceOperateService {

    @Resource
    AurineEdgeDataConnector aurineEdgeDataConnector;

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
    public AurineEdgeRespondDTO commandsDown(AurineEdgeConfigDTO configDTO, String deviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams) {
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

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO commandsDown(AurineEdgeConfigDTO configDTO, String deviceId, String serviceId, String commandName, JSONObject paramsJson, JSONObject otherParams, String msgId) {
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
        if(StringUtils.isNotBlank(msgId)){
            requestJson.put("msgId",msgId);
        }
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(),null,msgId);

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    /**
     * <p>
     * 下发命令同步
     * </p>
     */
    @Override
    public AurineEdgeRespondDTO syncCommandsDown(AurineEdgeConfigDTO configDTO, String productId, String deviceId, String serviceId, String commandName, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + deviceId + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfoJson = new JSONObject();

        commandInfoJson.put("serviceId", serviceId);
        commandInfoJson.put("commandName", commandName);

        requestJson.put("devId", deviceId);
        requestJson.put("commandInfo", commandInfoJson);
        requestJson.put("productId", productId);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));

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
    public AurineEdgeRespondDTO propertiesDown(AurineEdgeConfigDTO configDTO, String deviceId, String productId, JSONArray properties, JSONObject otherParams) {
        JSONObject requestJson = new JSONObject();
        String uri = "device/" + deviceId + "/properties/down";
        requestJson.put("productId", productId);
        requestJson.put("devId", deviceId);
        requestJson.put("properties", properties);
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }


    @Override
    public AurineEdgeRespondDTO messageDown(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String productId, JSONObject messageInfo, JSONObject otherParams) {
        JSONObject requestJson = new JSONObject();
        String uri = "device/" + thirdDeviceId + "/message/down";
        requestJson.put("devId", thirdDeviceId);
        requestJson.put("productId", productId);
        requestJson.put("messageInfo", messageInfo);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    /**
     * @return
     * @throws
     */
    @Override
    public AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams) {
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
    public AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams) {
        return this.objectManage(configDTO, null, thirdDeviceId, serviceId, action, objName, params, otherParams, null, null,null);
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
    public AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONArray params, JSONObject otherParams) {
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

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
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
    public AurineEdgeRespondDTO objectManageSync(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams) {
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


        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(), otherParams, null);
        String reqKey = "HUAWEI_SYNC_REQ_" + requestObject.getMessageId();
        String respKey = "HUAWEI_SYNC_RESP_" + requestObject.getMessageId();

        JSONObject respJson = aurineEdgeDataConnector.sendSync(requestObject, reqKey, respKey);
        AurineEdgeRespondDTO respondDTO = new AurineEdgeRespondDTO();
        respondDTO.setBodyObj(respJson);
        respondDTO.setErrorEnum(AurineEdgeErrorEnum.SUCCESS);

//        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.sendSync(requestObject, reqKey, respKey));
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
    public AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String msgId, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams, ProjectDeviceInfo projectDeviceInfo, List doorKeyIdList, Integer priotity) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("serviceId", serviceId);
        actionInfo.put("action", action);
        actionInfo.put("objName", objName);
        actionInfo.put("params", params);
        actionInfo.put("deviceInfo", projectDeviceInfo);
        actionInfo.put("doorKeyIdList", doorKeyIdList);
        actionInfo.put("priotity", priotity);

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);

        if (StringUtils.isNotEmpty(msgId)) {
            requestJson.put("msgId", msgId);
        }


        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(), otherParams, msgId);

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO mediaAdManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId, String serviceId, String action, String objName, JSONObject params, JSONObject otherParams) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();

        actionInfo.put("serviceId", serviceId);
        actionInfo.put("action", action);
        actionInfo.put("objName", objName);
        actionInfo.put("objInfo", params);

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion(), otherParams, null);
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO sendMessage(AurineEdgeConfigDTO config, NoticeMessageInfo infoObj, String thirdDeviceId) {
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = this.getClassManagerRequestJson("ADD", infoObj, thirdDeviceId, NoticeMessageInfo.getObjName(), UUID.randomUUID().toString().replaceAll("-", ""));

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(config, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    private JSONObject getClassManagerRequestJson(String action, Object objInfo, String thirdDeviceId, String objName, String msgId) {
        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();
        JSONObject paramsObj = new JSONObject();
        JSONArray messageObj = new JSONArray();
        messageObj.add(objInfo);
//        paramsObj.put("messageObj", messageObj);
        actionInfo.put("action", action);
        actionInfo.put("params", messageObj);
        actionInfo.put("objName", objName);
        actionInfo.put("msgId", msgId);
        actionInfo.put("serviceId", "InfoManager");

        requestJson.put("devId", thirdDeviceId);
        requestJson.put("actionInfo", actionInfo);
        requestJson.put("msgId", msgId);
        return requestJson;
    }

    @Override
    public AurineEdgeRespondDTO cleanMessage(AurineEdgeConfigDTO config, String thirdDeviceId) {
        String uri = "device/" + thirdDeviceId + "/objectManager/action";
        JSONObject requestJson = this.getClassManagerRequestJson(AurineEdgeCommandConstant.INFO_CLEAN, new JSONObject(), thirdDeviceId, InfoObj.getObjName(), UUID.randomUUID().toString().replaceAll("-", ""));
        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(config, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO objectManage(AurineEdgeConfigDTO configDTO, String thirdDeviceId,String productId,JSONObject jsonObject) {
        //生成请求对象
        String uri = "device/" + thirdDeviceId + "/objectManager/action";

        JSONObject requestJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();
        requestJson.put("devId", thirdDeviceId);
        requestJson.put("productId", productId);
        requestJson.put("actionInfo", actionInfo);

        actionInfo.put("objInfo", jsonObject);
        actionInfo.put("response", 0);
        actionInfo.put("objName", AurineEdgeObjNameEnum.PassObj.code);
        actionInfo.put("serviceId", AurineEdgeServiceEnum.PASS.code);
        actionInfo.put("action", "add");

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }


    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
