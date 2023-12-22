package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.StreetLightDeviceStatus;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.IotDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeCommandConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.policy.entity.BaseIotPolicy;
import com.aurine.cloudx.estate.thirdparty.module.device.policy.enums.DevicePolicyEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AurineEdgeIotDeviceServiceImplV1 implements IotDeviceService {

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_EDGE_MIDDLE.code;
    }

    @Override
    public void syncManholeCover() {

    }

    @Override
    public boolean lightAdjustment(StreetLightDeviceStatus streetLightDeviceStatus) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(streetLightDeviceStatus.getDeviceId(), AurineEdgeConfigDTO.class);
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(streetLightDeviceStatus.getDeviceId());
        ArrayNode properties = streetLightDeviceStatus.getProperties();
        AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer())
                .propertiesDown(config, deviceInfo.getThirdpartyCode(), deviceInfo.getProductId(), JSON.parseArray(properties.toString()), new JSONObject());
        return respondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS;
    }

    @Override
    public List<String> lightAdjustmentBatch(StreetLightDeviceStatus streetLightDeviceStatus) {
        List<String> deviceIdList = streetLightDeviceStatus.getDeviceIdList();
        List<String> successList = new ArrayList<>();
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.listByIds(deviceIdList);
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(streetLightDeviceStatus.getDeviceIdList().get(0), AurineEdgeConfigDTO.class);
        JSONArray array = JSON.parseArray(streetLightDeviceStatus.getProperties().toString());
        deviceInfoList.forEach(deviceInfo -> {
            AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer()).propertiesDown(config, deviceInfo.getThirdpartyCode(), deviceInfo.getProductId(), array, new JSONObject());
            if (AurineEdgeErrorEnum.SUCCESS.equals(respondDTO.getErrorEnum())) {
                successList.add(deviceInfo.getDeviceId());
            }
        });
        return successList;
    }

    @Override
    public void getDevicePoint() {

    }

    @Override
    public boolean sendDevicePolicy(String policyJson, String productId, String deviceType) {
        log.info("[冠林边缘网关-设备策略] 准备执行设备策略下发");
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                .eq(SysDeviceProductMap::getProductId, productId).last("limit 1"));
        Class clazz = DevicePolicyEnum.getClazz(DeviceManufactureEnum.getByManufactureAndDeviceType(productMap.getManufacture(), deviceType));
        if (clazz != null) {
            log.info("[冠林边缘网关-设备策略] 已获取到策略类Class对象：{}", clazz.getSimpleName());
            try {
                BaseIotPolicy policy = (BaseIotPolicy) objectMapper.readValue(policyJson, clazz);

                ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(policy.getSn());

                AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, deviceInfo.getProjectId(), 1, AurineEdgeConfigDTO.class);

                AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(),
                        AurineEdgeServiceEnum.DEVICE_CONTROL.code, AurineEdgeCommandConstant.DEVICE_CONTROL, JSONObject.parseObject(policy.getParams()), null);

                return respondDTO.getErrorEnum().equals(AurineEdgeErrorEnum.SUCCESS);
            } catch (JsonProcessingException e) {
                log.error("[冠林边缘网关 策略json解析异常] deviceType:{}; productId:{}; class:{}; JSON：{};", deviceType, productId, clazz.getSimpleName(), policyJson);
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }
}
