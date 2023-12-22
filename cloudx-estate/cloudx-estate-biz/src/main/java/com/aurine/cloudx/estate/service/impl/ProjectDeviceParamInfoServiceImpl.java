package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceParamInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamFormService;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.aurine.cloudx.estate.vo.DeviceParamForm;
import com.aurine.cloudx.estate.vo.ElevatorFloorInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;

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
        for (ProjectDeviceParamInfo paramInfo:
                deviceParamInfoList) {
            ProjectDeviceParamInfo byId = this.getById(paramInfo.getUuid());
            if(byId == null) {
                this.save(paramInfo);

            }else {
                this.updateParamInfo(paramInfo.getDeviceId(),paramInfo.getServiceId(),paramInfo.getDeviceParam());
            }
        }
//        this.saveOrUpdateBatch(deviceParamInfoList);
        /*List<ProjectDeviceParamInfo> deviceNoParamList = deviceParamInfoList.stream().filter(item -> item.getServiceId()
                .equals(DeviceParamEnum.DEVICE_NO_OBJ.getServiceId())).collect(Collectors.toList());*/
        List<ProjectDeviceInfo> projectDeviceInfoList = new ArrayList<>();
        ProjectDeviceInfo deviceInfo;
        if (CollUtil.isNotEmpty(deviceParamInfoList)) {
            for (ProjectDeviceParamInfo paramInfo : deviceParamInfoList) {
                //deviceInfo = new ProjectDeviceInfo();
                deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, paramInfo.getDeviceId()));
                String deviceParam = paramInfo.getDeviceParam();
                try {
                    ObjectNode objectNode = objectMapper.readValue(deviceParam, ObjectNode.class);
                    deviceInfo.setDeviceId(paramInfo.getDeviceId());
                    JsonNode deviceNo = objectNode.findPath("deviceNo");
                    boolean change = false;
                    if (!deviceNo.isMissingNode()) {
                        deviceInfo.setDeviceCode(deviceNo.asText());
                        change = true;
                    }
                    JsonNode ipAddr = objectNode.findPath("ipAddr");
                    if (!ipAddr.isMissingNode()) {
                        deviceInfo.setIpv4(ipAddr.asText());
                        change = true;
                    }
                    JsonNode floorSet = objectNode.findPath("floorSet");
                    if (!floorSet.isMissingNode()) {
                        projectDeviceAttrService.saveDeviceAttr(deviceInfo.getDeviceId(), "floorSet", "楼层设置", floorSet.asText());
                    }
                    JsonNode liftNo = objectNode.findPath("liftNo");
                    if (!liftNo.isMissingNode()) {
                        projectDeviceAttrService.saveDeviceAttr(deviceInfo.getDeviceId(), "liftNo", "电梯编号", liftNo.asText());
                    }
                    JsonNode openDoorMode = objectNode.findPath("openDoorMode");
                    if (!openDoorMode.isMissingNode()) {
                        projectDeviceAttrService.saveDeviceAttr(deviceInfo.getDeviceId(), "openDoorMode", "开门模式", openDoorMode.asText());
                    }
                    if (change) {
                        projectDeviceInfoList.add(deviceInfo);
                    }
                } catch (Exception e) {
                    log.error("设备参数解析异常，异常信息：{}", e.getCause());
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

    @Override
    public List<ProjectDeviceParamInfo> listValidDeviceParamInfo(String deviceId, String productId) {
        return baseMapper.listValidDeviceParamInfo(deviceId, productId);
    }

    @Override
    public ElevatorFloorInfo getFloorInfo(String floorSet) {
        if (StrUtil.isEmpty(floorSet)) {
            return new ElevatorFloorInfo();
        }

        String[] floorSetArr = floorSet.split(",");
        Set<String> floorNumSet = new HashSet<>();
        for (String item : floorSetArr) {
            String[] floorRange = item.split("~");
            // 如果只有一个说明
            if (floorRange.length == 1) {
                floorNumSet.add(floorRange[0]);
            }

            if (floorRange.length == 2) {
                int floor0 = Integer.parseInt(floorRange[0]);
                int floor1 = Integer.parseInt(floorRange[1]);
                int max = Math.max(floor1, floor0);
                int min = Math.min(floor1, floor0);
                if( max - min > 191) {
                    max = min + 191;
                }
                for (int floor = min; floor <= max && floorNumSet.size() <= 192; floor++) {
                    floorNumSet.add(String.valueOf(floor));
                }
            }
        }
        List<String> floorNumList = new ArrayList<>(floorNumSet).stream().sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList());
        Integer head = null;
        int headIndex = 0;
        StringBuilder newFloorSet = new StringBuilder();
        int maxSize = Math.min(192, floorNumList.size());
        for (int i = 0; i < maxSize; i++) {
            Integer current = Integer.parseInt(floorNumList.get(i));
            if (head == null) {
                head = current;
                newFloorSet.append(current);
                headIndex = i;
                if (floorNumList.size() == 1) {
                    newFloorSet.append(",");
                }
                continue;
            }
            Integer pre = Integer.parseInt(floorNumList.get(i - 1));
            // 说明是不连续的
            if (current -1 != pre) {
                if (headIndex != i - 1) {
                    newFloorSet.append("~").append(pre);
                }
                newFloorSet.append(",").append(current);
                head = current;
                headIndex = i;
            } else if (i == maxSize - 1){
                newFloorSet.append("~").append(current).append(",");
            }
        }
        floorSet = newFloorSet.toString();
        return new ElevatorFloorInfo(floorSet, floorNumList);
    }


    @Override
    public void setDefaultParam(ProjectDeviceInfo deviceInfo) {

    }

    @Override
    public boolean saveOrUpdateParam(List<ProjectDeviceParamInfo> paramInfoList) {
        paramInfoList.forEach(item -> {
            ProjectDeviceParamInfo deviceParamInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceParamInfo>().eq(ProjectDeviceParamInfo::getDeviceId, item.getDeviceId())
                    .eq(ProjectDeviceParamInfo::getServiceId, item.getServiceId()));
            if (deviceParamInfo != null) {
                item.setSeq(deviceParamInfo.getSeq());
            }
        });
        return this.saveOrUpdateBatch(paramInfoList);
    }

    @Override
    public void updateParamInfo(String deviceId, String serviceId, String deviceParam) {
        baseMapper.updateParamInfo(deviceId,serviceId,deviceParam);
    }
}
