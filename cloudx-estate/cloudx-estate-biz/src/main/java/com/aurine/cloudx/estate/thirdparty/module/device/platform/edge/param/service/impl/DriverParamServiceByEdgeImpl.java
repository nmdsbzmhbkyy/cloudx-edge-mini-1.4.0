package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service.AbstractParamServiceByEdge;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.AurineEdgeRemoteDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.DevicesResultVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceNoRule;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>边缘网关驱动设备的参数服务</p>
 *
 * @author : 王良俊
 * @date : 2021-10-19 17:20:12
 */
@Service
public class DriverParamServiceByEdgeImpl extends AbstractParamServiceByEdge {

    @Override
    public List<ProjectDeviceParamSetResultVo> deviceParamSetting(ObjectNode paramsNode, String deviceId) {
        log.info("[设备参数设置-{}] 设备ID：{} 参数：{}", getLogMark(), deviceId, paramsNode);
        List<ProjectDeviceParamSetResultVo> setResultList = new ArrayList<>();
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        AurineEdgeConfigDTO configByDeviceId = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);
        AurineEdgeRemoteDeviceService aurineEdgeRemoteDeviceService = AurineEdgeRemoteDeviceServiceFactory.getInstance(VersionEnum.V1);

        List<ProjectDeviceParamInfo> paramInfoList = projectDeviceParamInfoService.list(new LambdaQueryWrapper<ProjectDeviceParamInfo>().eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
        Map<String, ProjectDeviceParamInfo> paramInfoMap = paramInfoList.stream().collect(Collectors.toMap(ProjectDeviceParamInfo::getServiceId, deviceParamInfo -> deviceParamInfo, (o, o2) -> o2));
        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();
        paramsNode.fields().forEachRemaining(node -> {
            AurineEdgeRespondDTO aurineEdgeRespondDTO = aurineEdgeRemoteDeviceService.setEdgeParam(configByDeviceId, deviceInfo, JSON.parseObject(node.getValue().toString()), node.getKey());
            boolean isSuccess = AurineEdgeErrorEnum.SUCCESS.equals(aurineEdgeRespondDTO.getErrorEnum());
            String serviceId = node.getKey();
            setResultList.add(new ProjectDeviceParamSetResultVo(deviceId, deviceInfo.getThirdpartyCode(), serviceId, isSuccess));

            if (isSuccess) {
                ProjectDeviceParamInfo deviceParamInfo = paramInfoMap.get(node.getKey());
                if (deviceParamInfo == null) {
                    deviceParamInfo = this.initProjectDeviceParamInfo(deviceId, serviceId, (ObjectNode) node.getValue());
                } else {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.set(node.getKey(), node.getValue());
                    deviceParamInfo.setDeviceParam(objectNode.toString());
                }
                deviceParamInfoList.add(deviceParamInfo);
            }
        });
        projectDeviceParamInfoService.saveOrUpdateBatch(deviceParamInfoList);
        return setResultList;
    }

    @Override
    protected String getLogMark() {
        return "驱动设备";
    }


    @Override
    public DevicesResultVo multiDeviceParamSetting(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        return null;
    }

    /**
     * {
     * "onNotifyData":{
     * "devId":"2f491218-4943-430d-95f7-c2eaa5a4aa0e",
     * "gatewayId":"2f491218-4943-430d-95f7-c2eaa5a4aa0e",
     * "msgId":"470545e7-78b9-4a77-96e6-e4cf1dd4474f",
     * "objManagerData":{
     * "objInfo":{
     * "devSubDesc":[
     * "栋",
     * "单元",
     * "房"
     * ],
     * "devNoRule":{
     * "useCellNo":1,
     * "roomNoLen":4,
     * "cellNoLen":2,
     * "subSection":"224",
     * "stairNoLen":4
     * }
     * },
     * "objName":"DeviceNoObj",
     * "serviceId":"DeviceParams"
     * },
     * "productId":"3G99GW01HA"
     * },
     * "resource":"device.objmanager",
     * "event":"report",
     * "subscriptionId":1442385202919096357
     * }
     */
    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {
        try {
            ObjectNode rootNode = objectMapper.readValue(json, ObjectNode.class);
            ObjectNode objInfo = (ObjectNode) rootNode.findPath("objInfo");
            String serviceId = rootNode.findPath("objName").asText();
            ObjectNode paramNode = objectMapper.createObjectNode();
            paramNode.set(serviceId, objInfo);

            if (DeviceParamEnum.EDGE_DEVICE_NO_OBJ.serviceId.equals(serviceId)) {

                if (RedisUtil.hasKey(deviceInfo.getDeviceId() + DeviceParamEnum.EDGE_DEVICE_NO_OBJ.serviceId)) {
                    return;
                }
                // 存入redis 如果30秒内再次接收到这个参数就不处理
                RedisUtil.set(deviceInfo.getDeviceId() + DeviceParamEnum.EDGE_DEVICE_NO_OBJ.serviceId, "1", 30);
                ProjectDeviceNoRule projectSubSection = projectEntityLevelCfgService.getProjectSubSection(ProjectContextHolder.getProjectId());
                int projectStairNoLen = projectSubSection.getStairNoLen();
                int projectRoomNoLen = projectSubSection.getRoomNoLen();
                int projectCellNoLen = projectSubSection.getCellNoLen();
                String sysSubSection = projectSubSection.getSubSection();

                ObjectNode devNoRule = (ObjectNode) objInfo.findPath("devNoRule");
                int stairNoLen = devNoRule.path("stairNoLen").asInt();
                int roomNoLen = devNoRule.path("roomNoLen").asInt();
                int cellNoLen = devNoRule.path("cellNoLen").asInt();
                String subSection = devNoRule.path("subSection").asText();
                boolean isDifferent = stairNoLen != projectStairNoLen || roomNoLen != projectRoomNoLen || cellNoLen != projectCellNoLen;
                ArrayNode devSubDesc = (ArrayNode) objInfo.path("devSubDesc");
                List<String> projectSubSectionDesc = projectSubSection.getSubSectionDesc();
                if (projectSubSectionDesc.size() == devSubDesc.size()) {
                    for (int i = 0; i < projectSubSectionDesc.size(); i++) {
                        if (!projectSubSectionDesc.get(i).equals(devSubDesc.get(i).asText())) {
                            isDifferent = true;
                            break;
                        }
                    }
                } else {
                    isDifferent = true;
                }
                if (!sysSubSection.equals(subSection)) {
                    isDifferent = true;
                }
                if (isDifferent) {
                    log.info("[设备参数-驱动设备] 设备编号规则与项目配置不同 设备ID：{}", deviceInfo.getDeviceId());
                    devNoRule.put("stairNoLen", projectStairNoLen);
                    devNoRule.put("roomNoLen", projectRoomNoLen);
                    devNoRule.put("cellNoLen", projectCellNoLen);
                    objInfo.putPOJO("devSubDesc", projectSubSectionDesc);
                    devNoRule.put("subSection", sysSubSection);
                    deviceParamSetting(paramNode, deviceInfo.getDeviceId());
                }
            }

            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
            ProjectDeviceParamInfo deviceParamInfo = initProjectDeviceParamInfo(deviceInfo.getDeviceId(), serviceId, paramNode);
            projectDeviceParamInfoService.saveOrUpdate(deviceParamInfo);
            log.info("参数数据：{}", rootNode);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.contains("MissingNode")) {
                log.info("[设备参数] 该事件不属于设备参数上报 json:{}", json);
            } else {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<DeviceManufactureEnum> getApplicableDeviceProducts() {
        Set<DeviceManufactureEnum> set = new HashSet<>();
        set.add(DeviceManufactureEnum.MILI_DRIVER_DEVICE_EDGE);
        return set;
    }

    @Override
    protected void deviceParamPreHandle(String serviceId, ObjectNode paramObj) {

    }

    @Override
    public void requestDeviceParam(Set<String> serviceIdSet, String deviceId) {
        log.info("正在请求驱动设备参数 deviceId：{} serviceId：{}", deviceId, serviceIdSet);
        serviceIdSet.remove(DeviceParamEnum.DEV_RULE_OBJ.objName);
        AurineEdgeConfigDTO configByDeviceId = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        AurineEdgeRemoteDeviceServiceFactory.getInstance(VersionEnum.V1).getEdgeParam(configByDeviceId, deviceInfo, serviceIdSet);
    }

}
