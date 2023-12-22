package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.param.service;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.AbstractDeviceParamService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiServiceEnum;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * <p>设备参数服务-边缘网关中台抽象类</p>
 *
 * @author : 王良俊
 * @date : 2021-10-19 17:06:24
 */
public abstract class AbstractParamServiceByEdge extends AbstractDeviceParamService {

    @Override
    public abstract Set<DeviceManufactureEnum> getApplicableDeviceProducts();

    @Override
    public List<ProjectDeviceParamSetResultVo> deviceParamSetting(ObjectNode paramsNode, String deviceId) {
        if (paramsNode.isMissingNode() || StringUtils.isEmpty(deviceId)) {
            throw new RuntimeException("缺少必要参数无法进行本次设置");
        }

        List<Map.Entry<String, JsonNode>> finalParamList = new ArrayList<>();

        log.info("[设备参数设置-{}] 开始设置设备参数，项目ID：{}，设备ID：{}", getLogMark(), ProjectContextHolder.getProjectId(), deviceId);

        // 参数设置失败的设备ID列表
        List<ProjectDeviceParamSetResultVo> resultList = new ArrayList<>();
        // 这里获取到设备信息对象列表
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));

        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();

        if (deviceInfo != null) {

            // 获取到可能导致重启的参数serviceId
            List<String> rebootServiceIdList = sysServiceParamConfService.getRebootServiceIdList();

            rebootServiceIdList.forEach(serviceId -> {
                JsonNode node = paramsNode.findPath(serviceId);
                if (!node.isMissingNode()) {
                    finalParamList.add(new Map.Entry<String, JsonNode>() {
                        @Override
                        public String getKey() {
                            return serviceId;
                        }

                        @Override
                        public JsonNode getValue() {
                            return node;
                        }

                        @Override
                        public JsonNode setValue(JsonNode value) {
                            return null;
                        }
                    });
                }
            });

            paramsNode.fields().forEachRemaining(nodeEntry -> {
                // 这里判断如果有网络参数则放到最后执行
                if (!rebootServiceIdList.contains(nodeEntry.getKey()) && !DeviceParamEnum.getIsNotADeviceParameterServiceID().contains(nodeEntry.getKey())) {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.set(nodeEntry.getKey(), nodeEntry.getValue());
                    resultList.add(sendDeviceParam(deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode(),
                            objectNode, nodeEntry.getKey(), deviceParamInfoList));
                }
            });

            if (CollUtil.isNotEmpty(finalParamList)) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                finalParamList.forEach(nodeEntry -> {
                    objectNode.set(nodeEntry.getKey(), nodeEntry.getValue());
                });
                resultList.addAll(sendDeviceParam(deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode(), objectNode,
                        deviceParamInfoList));
            }
        } else {
            log.error("[设备参数设置-{}] 未找到设备ID为[{}]的设备", getLogMark(), deviceId);
        }

        // 这里保存设置成功的设备参数数据
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            projectDeviceParamInfoService.saveDeviceParamBatch(deviceParamInfoList);
        }
        return resultList;
    }

    @Override
    public void requestDeviceParam(Set<String> serviceIdList, String deviceId) {
        Set<String> serviceIdSet = new HashSet<>(serviceIdList);
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        serviceIdSet.forEach(serviceId -> {
            DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService()
                    .getDeviceParam(DeviceParamEnum.getObjName(serviceId), deviceInfo.getThirdpartyCode(), deviceInfo.getDeviceId());
        });
    }

    @Override
    public PlatformEnum getPlateForm() {
        return PlatformEnum.AURINE_EDGE_MIDDLE;
    }
}
