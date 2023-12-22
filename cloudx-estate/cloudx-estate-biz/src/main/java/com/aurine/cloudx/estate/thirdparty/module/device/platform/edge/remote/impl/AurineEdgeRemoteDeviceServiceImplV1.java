package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.AurineEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.core.util.AurineEdgeUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.AurineEdgeRemoteDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.EdgeCascadeConfVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

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
public class AurineEdgeRemoteDeviceServiceImplV1 implements AurineEdgeRemoteDeviceService {
    @Resource
    AurineEdgeDataConnector aurineEdgeDataConnector;

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    @Resource
    private EdgeCascadeConfService edgeCascadeConfService;

    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectInfoService projectInfoService;

    /**
     * 新增一台设备
     *
     * @param configDTO
     * @param deviceInfoDTO
     * @return
     */
    @Override
    public AurineEdgeRespondDTO addDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO) {


        //生成请求对象
        String uri = "device/" + deviceInfoDTO.getDevId() + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfo = new JSONObject();
        JSONObject params = new JSONObject();


        commandInfo.put("serviceId", AurineEdgeServiceEnum.DEVICE_INFO.code);
        commandInfo.put("commandName", AurineEdgeActionConstant.ADD);
        commandInfo.put("params", deviceInfoDTO);

        EdgeCascadeConfVo conf = edgeCascadeConfService.getConf(ProjectContextHolder.getProjectId());
        Character cloudStatus = conf.getCloudStatus();
        /*if (IntoCloudStatusEnum.INTO_CLOUD.code == cloudStatus || IntoCloudStatusEnum.UNBINDING.code == cloudStatus) {
            String projectUUID = projectInfoService.getProjectUUID(ProjectContextHolder.getProjectId());
            commandInfo.put("communityId", StrUtil.isNotEmpty(projectUUID) ? projectUUID : "");
        }*/

        String projectUUID = projectInfoService.getProjectUUID(ProjectContextHolder.getProjectId());
        commandInfo.put("communityId", StrUtil.isNotEmpty(projectUUID) ? projectUUID : "");

        requestJson.put("productId", deviceInfoDTO.getProductId());
        requestJson.put("devId", deviceInfoDTO.getDevId());
        requestJson.put("commandInfo", commandInfo);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    /**
     * 激活一台设备
     *
     * @param configDTO
     * @param deviceInfoDTO
     */
    @Override
    public AurineEdgeRespondDTO activeDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO) {

        //生成请求对象
        String uri = "device/" + deviceInfoDTO.getDevId() + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfo = new JSONObject();
        JSONObject params = new JSONObject();


        commandInfo.put("serviceId", AurineEdgeServiceEnum.DEVICE_INFO.code);
        commandInfo.put("commandName", AurineEdgeActionConstant.ACTIVE);
        commandInfo.put("params", deviceInfoDTO);


        requestJson.put("productId", deviceInfoDTO.getProductId());
        requestJson.put("devId", deviceInfoDTO.getDevId());
        requestJson.put("commandInfo", commandInfo);


        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO addDevice(AurineEdgeConfigDTO configDTO, String uuid, String productKey) {
        return this.addDeviceBatch(configDTO, new String[]{uuid}, productKey);
    }

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     * @return
     */
    @Override
    public AurineEdgeRespondDTO addDeviceBatch(AurineEdgeConfigDTO configDTO, String[] uuidArray) {

        //生成请求对象
        String uri = "device/add";
        JSONObject requestJson = new JSONObject();
        JSONObject uuidObjJson = new JSONObject();
        JSONArray uuidObjJsonArray = new JSONArray();

        for (String uuid : uuidArray) {
            uuidObjJson = new JSONObject();
            uuidObjJson.put("uuid", uuid);
            uuidObjJsonArray.add(uuidObjJson);
        }

        requestJson.put("devices", uuidObjJsonArray);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());


        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    /**
     * 批量新增多态设备
     *
     * @param configDTO
     * @param uuidArray
     * @return
     */
    @Override
    public AurineEdgeRespondDTO addDeviceBatch(AurineEdgeConfigDTO configDTO, String[] uuidArray, String productKey) {

        //生成请求对象
        String uri = "device/add";
        JSONObject requestJson = new JSONObject();
        JSONObject uuidObjJson = new JSONObject();
        JSONArray uuidObjJsonArray = new JSONArray();

        for (String uuid : uuidArray) {
            uuidObjJson = new JSONObject();
            uuidObjJson.put("uuid", uuid);
            uuidObjJson.put("productKey", productKey);
            uuidObjJsonArray.add(uuidObjJson);
        }

        requestJson.put("devices", uuidObjJsonArray);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());


        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    /**
     * 删除设备
     *
     * @param configDTO
     * @return
     */
    @Override
    public AurineEdgeRespondDTO delDevice(AurineEdgeConfigDTO configDTO, AurineEdgeDeviceInfoDTO deviceInfoDTO) {
//        String uri = "device/" + deviceThirdId + "/del";


        //生成请求对象
        String uri = "device/" + deviceInfoDTO.getDevId() + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfo = new JSONObject();
        JSONObject params = new JSONObject();


        commandInfo.put("serviceId", AurineEdgeServiceEnum.DEVICE_INFO.code);
        commandInfo.put("commandName", AurineEdgeActionConstant.DELETE);
        commandInfo.put("params", deviceInfoDTO);


        requestJson.put("productId", deviceInfoDTO.getProductId());
        requestJson.put("devId", deviceInfoDTO.getDevId());
        requestJson.put("commandInfo", commandInfo);


//        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, new JSONObject(), this.getVersion());
//        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        //return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
        String reqKey = AurineEdgeSyncReqEnum.EDGE_SYNC_REQ.code + requestObject.getMessageId();
        String respKey = AurineEdgeSyncReqEnum.EDGE_SYNC_RESP.code + requestObject.getMessageId();
        return AurineEdgeDeviceDataUtil.handelRespondDeleteDevice(aurineEdgeDataConnector.sendSync(requestObject,reqKey,respKey));
    }

    /**
     * 查询产品列表
     *
     * @param configDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public AurineEdgeRespondDTO queryProducts(AurineEdgeConfigDTO configDTO, Integer pageNo, Integer pageSize) {
        //init
        StringBuilder queryParams = new StringBuilder();
        if (pageNo != null && pageNo >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageNo=" + pageNo);
        }
        if (pageSize != null && pageSize >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageSize=" + pageSize);
        }

        //生成请求对象
        String uri = "products/query" + queryParams.toString();
        JSONObject requestJson = new JSONObject();

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createGetRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO queryDevices(AurineEdgeConfigDTO configDTO, Integer pageNo, Integer pageSize, String productId, String fDevId) {
        StringBuilder queryParams = new StringBuilder();
        if (pageNo != null && pageNo >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageNo=" + pageNo);
        }
        if (pageSize != null && pageSize >= 1) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("pageSize=" + pageSize);
        }
        if (StrUtil.isNotEmpty(productId)) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("productId=" + productId);
        }
        if (StrUtil.isNotEmpty(fDevId)) {
            queryParams.append(queryParams.length() == 0 ? "?" : "&").append("fDevId=" + fDevId);
        }
        //生成请求对象
        String uri = "device/query" + queryParams.toString();
        JSONObject requestJson = new JSONObject();

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createGetRequestObject(configDTO, uri, requestJson, getVersion());

        return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
    }

    @Override
    public AurineEdgeRespondDTO setEdgeParam(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, JSONObject params, String objName) {

        //生成请求对象
        String uri = "device/" + deviceInfo.getThirdpartyCode() + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfo = new JSONObject();

        commandInfo.put("serviceId", "DeviceParams");
        commandInfo.put("commandName", AurineEdgeActionConstant.SET);
        commandInfo.put("objInfo", params);
        commandInfo.put("objName", objName);

        requestJson.put("productId", deviceInfo.getProductId());
        requestJson.put("devId", deviceInfo.getThirdpartyCode());
        requestJson.put("commandInfo", commandInfo);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        String msgId = requestJson.getString("msgId");
        JSONObject paramObj = new JSONObject();
        paramObj.put(objName, params);
        RedisUtil.set(AurineEdgeParamCallbackConstant.REDIS_KEY_PRE_STR + msgId, paramObj.toString(), 60);
        try {
            return AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("边缘网关-参数设置失败 serviceId：{} paramData：{}", objName, params);
            AurineEdgeRespondDTO respondDTO = new AurineEdgeRespondDTO();
            respondDTO.setErrorEnum(AurineEdgeErrorEnum.FAIL);
            return respondDTO;
        }
    }

    @Override
    public void getEdgeParam(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, Collection<String> serviceIdList) {
        serviceIdList.forEach(objName -> {
            try {
                //生成请求对象
                String uri = "device/" + deviceInfo.getThirdpartyCode() + "/commands/syncdown";
                JSONObject requestJson = new JSONObject();
                JSONObject commandInfo = new JSONObject();
                SysDeviceProductMap productMap = sysDeviceProductMapService.getById(deviceInfo.getProductId());

                commandInfo.put("serviceId", "DeviceParams");
                commandInfo.put("commandName", AurineEdgeActionConstant.GET);
                commandInfo.put("objName", objName);
                commandInfo.put("objInfo", new JSONObject());
//                requestJson.put("productId", deviceInfo.getProductId());
                requestJson.put("devId", deviceInfo.getThirdpartyCode());
                requestJson.put("commandInfo", commandInfo);
                AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
                AurineEdgeDeviceDataUtil.handelRespond(aurineEdgeDataConnector.send(requestObject));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CallBackData sendDataByDeviceInfo(AurineEdgeConfigDTO configDTO, ProjectDeviceInfo deviceInfo, String commandName, AurineEdgeDeviceInfoDTO param) {
        //生成请求对象
        String uri = "device/" + deviceInfo.getThirdpartyCode() + "/commands/syncdown";
        JSONObject requestJson = new JSONObject();
        JSONObject commandInfo = new JSONObject();

        commandInfo.put("serviceId", AurineEdgeServiceEnum.DEVICE_INFO.code);
        commandInfo.put("commandName", commandName);
        commandInfo.put("params", param);

        requestJson.put("productId", param.getProductId());
        requestJson.put("devId", deviceInfo.getThirdpartyCode());
        requestJson.put("commandInfo", commandInfo);

        AurineEdgeRequestObject requestObject = AurineEdgeUtil.createPostRequestObject(configDTO, uri, requestJson, getVersion());
        String reqKey = AurineEdgeSyncReqEnum.EDGE_SYNC_REQ.code + requestObject.getMessageId();
        String respKey = AurineEdgeSyncReqEnum.EDGE_SYNC_RESP.code + requestObject.getMessageId();
        return AurineEdgeDeviceDataUtil.handelRespondForCallback(aurineEdgeDataConnector.sendSync(requestObject,reqKey,respKey));
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


}
