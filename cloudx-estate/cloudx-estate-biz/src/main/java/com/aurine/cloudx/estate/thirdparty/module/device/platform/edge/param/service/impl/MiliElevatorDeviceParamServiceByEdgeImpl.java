package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service.AbstractParamServiceByEdge;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.vo.DevicesResultVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MiliElevatorDeviceParamServiceByEdgeImpl extends AbstractParamServiceByEdge {

    // 电梯分层控制器不使用动态表单的方式生成
    @Override
    public String getParamFormJson(List<String> serviceIdList, String deviceId) {
        return "";
    }

    @Override
    protected String getLogMark() {
        return "米立电梯分层控制器";
    }

    @Override
    protected void deviceParamPreHandle(String serviceId, ObjectNode paramObj) {

    }

    @Override
    public DevicesResultVo multiDeviceParamSetting(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        if (CollUtil.isEmpty(serviceIdList)) {
            DevicesResultVo devicesResultVo = new DevicesResultVo();
            devicesResultVo.setFailedNum(0);
            devicesResultVo.setSuccessNum(deviceIdList.size());
            devicesResultVo.setTotalNum(deviceIdList.size());
            return devicesResultVo;
        }
        Iterator<Map.Entry<String, JsonNode>> paramFields = paramsNode.fields();
        List<ProjectDeviceParamInfo> originDeviceParamInfoList = projectDeviceParamInfoService.list(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
                .in(ProjectDeviceParamInfo::getDeviceId, deviceIdList).in(ProjectDeviceParamInfo::getServiceId, serviceIdList));
        Map<String, ProjectDeviceParamInfo> deviceParamInfoMap = originDeviceParamInfoList.stream().collect(Collectors.toMap(paramInfo -> paramInfo.getDeviceId() + paramInfo.getServiceId(),
                projectDeviceParamInfo -> projectDeviceParamInfo, (val1, val2) -> val1));
        Map<String, ObjectNode> paramNodeMap = new HashMap<>();
        while (paramFields.hasNext()) {
            Map.Entry<String, JsonNode> next = paramFields.next();
            String key = next.getKey();
            ObjectNode paramNode = (ObjectNode)next.getValue();
            if (serviceIdList.contains(key)) {
                if (DeviceParamEnum.EDGE_LIFT_DEVICE_PARAMS_OBJ.serviceId.equals(key)) {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.set("building", paramNode.path("building"));
                    objectNode.set("unit", paramNode.path("unit"));
                    objectNode.set("secretKey", paramNode.path("secretKey"));
                    paramNode = objectNode;
                } else if (DeviceParamEnum.EDGE_LIFT_NETWORK_PARAMS_OBJ.serviceId.equals(key)) {
                    paramNode.remove("mac");
                    paramNode.remove("ipAddr");
                }
                paramNodeMap.put(key, paramNode);
            }
        }
        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, deviceIdList));
        Map<String, List<String>> faildMap = new HashMap<>();
        deviceInfoList.forEach(deviceInfo -> {
            serviceIdList.forEach(serviceId -> {
                ProjectDeviceParamInfo paramInfo = deviceParamInfoMap.get(deviceInfo.getDeviceId() + serviceId);
                ObjectNode templateParam = paramNodeMap.get(serviceId);
                if (paramInfo != null && templateParam != null) {
                    try {
                        ObjectNode originNode = objectMapper.readValue(paramInfo.getDeviceParam(), ObjectNode.class);
                        recursivelyJsonTree(originNode, templateParam);
                        List<ProjectDeviceParamSetResultVo> resultList = this.sendDeviceParam(deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode(), originNode, deviceParamInfoList);
                        resultList.forEach(result -> {
                            if (!result.isSuccess()) {
                                saveParamSetResult(faildMap, result);
                            }
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }

            });
        });
//        recursivelyJsonTree();
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            projectDeviceParamInfoService.saveDeviceParamBatch(deviceParamInfoList);
        }
        return new DevicesResultVo(deviceIdList.size() - faildMap.size(), faildMap.size(), deviceIdList.size(), new ArrayList<>(faildMap.keySet()));
    }

    @Override
    public void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo) {

        try {
            ObjectNode rootNode = objectMapper.readValue(json, ObjectNode.class);
            ObjectNode objInfo = (ObjectNode) rootNode.findPath("objInfo");
            String serviceId = rootNode.findPath("objName").asText();
            if(StringUtils.isBlank(serviceId)){
                return;
            }
            ObjectNode paramNode = objectMapper.createObjectNode();
            paramNode.set(serviceId, objInfo);
            List<String> serviceIdList = new ArrayList<>();
            serviceIdList.add(serviceId);
            JsonNode newParam = deviceParamDataHandleChain.handle(serviceIdList, paramNode.toString(), getPlateForm(), true);
            List<ProjectDeviceParamInfo> paramInfoList = new ArrayList<>();
            ProjectDeviceParamInfo paramInfo = new ProjectDeviceParamInfo();
            paramInfo.setDeviceParam(newParam.toString());
            paramInfo.setDeviceId(deviceInfo.getDeviceId());
            paramInfo.setServiceId(serviceId);
            paramInfo.setProjectId(deviceInfo.getProjectId());
            paramInfo.setTenant_id(1);
            ProjectDeviceParamInfo existParamInfo = projectDeviceParamInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceParamInfo>().eq(ProjectDeviceParamInfo::getDeviceId, paramInfo.getDeviceId()).eq(ProjectDeviceParamInfo::getServiceId, paramInfo.getServiceId()));
            if (existParamInfo != null) {
                paramInfo.setUuid(existParamInfo.getUuid());
            }
            paramInfoList.add(paramInfo);
            projectDeviceParamInfoService.saveDeviceParamBatch(paramInfoList);
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
        Set<DeviceManufactureEnum> deviceManufactureEnumSet = new HashSet<>();
        deviceManufactureEnumSet.add(DeviceManufactureEnum.MILI_ELEVATOR_LAYER_CONTROL_DEVICE);
        return deviceManufactureEnumSet;
    }


}