package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.origin.vo.DeviceParamDataVo;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceParamInfoMapper;
import com.aurine.cloudx.open.origin.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.open.origin.service.SysProductServiceService;
import com.aurine.cloudx.open.origin.service.SysServiceParamConfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表服务实现类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:15
 */
@Service
public class ProjectDeviceParamInfoServiceImpl extends ServiceImpl<ProjectDeviceParamInfoMapper, ProjectDeviceParamInfo>
        implements ProjectDeviceParamInfoService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Resource
    private SysProductServiceService sysProductServiceService;
    @Resource
    private SysServiceParamConfService sysServiceParamConfService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    public DeviceParamDataVo getParamByDeviceId(String deviceId, String productId) {
        List<ProjectDeviceParamInfo> deviceParamInfoList = this.baseMapper.listValidDeviceParamInfo(deviceId, productId);
//        List<ProjectDeviceParamInfo> deviceParamInfoList = this.list(new QueryWrapper<ProjectDeviceParamInfo>().lambda()
//                .eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            DeviceParamDataVo deviceParamDataVo = new DeviceParamDataVo();
            List<String> paramList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getDeviceParam).collect(Collectors.toList());
            List<String> serviceIdList = deviceParamInfoList.stream().map(ProjectDeviceParamInfo::getServiceId).collect(Collectors.toList());
            List<String> paramNameList = new ArrayList<>();
            // 这里将原本分开存储的参数json数据整合成一个json数据 （和前端传给后端的json数据格式一致）
            ObjectNode rootNode = objectMapper.createObjectNode();
            paramList.forEach(paramJson -> {
                if (StrUtil.isNotEmpty(paramJson)) {
                    try {
                        ObjectNode param = (ObjectNode) objectMapper.readTree(paramJson);
                        param.fields().forEachRemaining(item -> {
                            if (StrUtil.isNotEmpty(item.getKey())) {
                                // 这里设备编号参数比较特殊里面还带有另一个参数对象
                                rootNode.set(item.getKey(), item.getValue());
                                paramNameList.add(item.getKey());
                            }
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
            deviceParamDataVo.setParamData(rootNode.toString());
            deviceParamDataVo.setParamNameList(paramNameList);
            deviceParamDataVo.setServiceIdList(serviceIdList);
            System.out.println("dataJson:" + rootNode.toString());
            return deviceParamDataVo;
        }

        return null;
    }

    @Override
    public boolean refreshParam(ProjectDeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return false;
        }
        if (!"1".equals(deviceInfo.getStatus())) {
            return false;
        }

        projectDeviceInfoService.initDeviceParamData(deviceInfo.getDeviceId());
        /*List<SysProductService> productServiceList = sysProductServiceService.list(new QueryWrapper<SysProductService>().lambda()
                .eq(SysProductService::getProductId, deviceInfo.getProductId())
                .notIn(SysProductService::getServiceId, DeviceParamEnum.getIsNotADeviceParameterServiceID()));
        if (CollUtil.isNotEmpty(productServiceList)) {
            Set<String> serviceIdSet = productServiceList.stream().map(SysProductService::getServiceId).collect(Collectors.toSet());
            List<SysServiceParamConf> existServiceList = sysServiceParamConfService.list(new QueryWrapper<SysServiceParamConf>()
                    .lambda().in(SysServiceParamConf::getServiceId, serviceIdSet));

            if (CollUtil.isNotEmpty(existServiceList)) {
                // 这里获取到的是设备可以进行获取的参数数据
                Set<String> existServiceIdSet = existServiceList.stream()
                        .map(sysServiceParamConf -> DeviceParamEnum.getObjName(sysServiceParamConf.getServiceId()))
                        .collect(Collectors.toSet());
                existServiceIdSet.forEach(serviceId -> {
                    DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(),
                            ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService()
                            .getDeviceParam(serviceId, deviceInfo.getThirdpartyCode(), deviceInfo.getDeviceId());

                });
            }
        }
        // 获取设备信息
        DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(),
                ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService()
                .getDeviceInfo(deviceInfo.getDeviceId(), deviceInfo.getThirdpartyCode());*/
        return true;
    }

    @Override
    public void saveDeviceParamBatch(List<ProjectDeviceParamInfo> deviceParamInfoList) {
        this.saveOrUpdateBatch(deviceParamInfoList);
        List<ProjectDeviceParamInfo> deviceNoParamList = deviceParamInfoList.stream().filter(item -> item.getServiceId()
                .equals(DeviceParamEnum.DEVICE_NO_OBJ.getServiceId())).collect(Collectors.toList());
        List<ProjectDeviceInfo> projectDeviceInfoList = new ArrayList<>();
        ProjectDeviceInfo deviceInfo;
        if (CollUtil.isNotEmpty(deviceNoParamList)) {
            for (ProjectDeviceParamInfo paramInfo: deviceNoParamList) {
                deviceInfo = new ProjectDeviceInfo();
                String deviceParam = paramInfo.getDeviceParam();
                try {
                    ObjectNode objectNode = objectMapper.readValue(deviceParam, ObjectNode.class);
                    deviceInfo.setDeviceId(paramInfo.getDeviceId());
                    JsonNode deviceNo = objectNode.findPath("deviceNo");
                    deviceInfo.setDeviceCode(deviceNo.isMissingNode() ? "" : deviceNo.asText());
                    projectDeviceInfoList.add(deviceInfo);
                } catch (Exception e) {
                    log.error("设备编号参数解析异常，异常信息：{}",e.getCause());
                }
            }
            // 更新设备编号到设备信息表
            if (CollUtil.isNotEmpty(projectDeviceInfoList)) {
                projectDeviceInfoService.updateBatchById(projectDeviceInfoList);
            }
        }
    }

    @Override
    public String getDeviceOtherStatus(String deviceId) {
        if (StrUtil.isEmpty(deviceId)) {
            throw new RuntimeException("缺少设备ID");
        }
        ProjectDeviceParamInfo deviceParamInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceParamInfo>().eq(ProjectDeviceParamInfo::getDeviceId, deviceId)
                .eq(ProjectDeviceParamInfo::getServiceId, DeviceParamEnum.DEVICE_STATE_CHANGE.getServiceId()));
        return deviceParamInfo != null && StrUtil.isNotEmpty(deviceParamInfo.getDeviceParam()) ? deviceParamInfo.getDeviceParam() : "{}";
    }
}