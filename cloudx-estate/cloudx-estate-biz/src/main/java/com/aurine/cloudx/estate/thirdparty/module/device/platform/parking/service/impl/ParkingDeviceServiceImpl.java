package com.aurine.cloudx.estate.thirdparty.module.device.platform.parking.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingTextModelEnum;
import com.aurine.cloudx.estate.dto.DeviceExtendAttr;
import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.dto.ProjectDeviceInfoDto;
import com.aurine.cloudx.estate.dto.param.*;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.vo.*;
import com.aurine.parking.feign.RemoteParkingDeviceService;
import com.aurine.parking.util.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 只用做车场服务
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/2 15:08
 */
@Slf4j
@Service
public class ParkingDeviceServiceImpl implements DeviceService {

    @Resource
    RemoteParkingDeviceService remoteParkingDeviceService;

    @Resource
    SysDeviceProductMapService sysDeviceProductMapService;

    @Resource
    private ProjectDeviceAttrService deviceAttrService;

    @Resource
    private ProjectDeviceRegionService deviceRegionService;

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_PARKING.code;
    }

    public PlatformEnum getPlatformEnum() {
        return PlatformEnum.AURINE_PARKING;
    }

    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device) {
        String ipv4 = device.getIpv4();
        String productId = device.getProductId();
        if (StrUtil.isEmpty(ipv4)) {
            throw new RuntimeException("缺少必要参数设备IPV4地址，无法添加设备");
        }
        if (StrUtil.isEmpty(productId)) {
            throw new RuntimeException("未获取到设备产品信息，无法添加设备");
        }
        SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductId, productId));

        ProjectDeviceInfoDto deviceInfoDto = new ProjectDeviceInfoDto();
        deviceInfoDto.setIpv4(ipv4);
        deviceInfoDto.setProductKey(sysDeviceProductMap.getProductCode());
        R<ProjectDeviceInfoDto> response = remoteParkingDeviceService.addDevice(deviceInfoDto, SecurityConstants.FROM_IN);
        ProjectDeviceInfoDto responseDeviceInfo = response.getData();
        List<DeviceExtendAttr> otherParamList = responseDeviceInfo.getOtherParamList();
        // 这里存放的是拓展属性（车道一体机的视频Url就在这里面）
        if (CollUtil.isNotEmpty(otherParamList)) {
            List<ProjectDeviceAttrVo> deviceAttrList = otherParamList.stream().map(deviceExtendAttr -> {
                ProjectDeviceAttrVo deviceAttr = new ProjectDeviceAttrVo();
                deviceAttr.setAttrCode(deviceExtendAttr.getAttrName());
                deviceAttr.setAttrValue(deviceExtendAttr.getAttrValue());
                if ("videoUrl".equals(deviceExtendAttr.getAttrName())) {
                    deviceAttr.setAttrName("视频URL");
                } else {
                    deviceAttr.setAttrName(deviceExtendAttr.getAttrName());
                }
                return deviceAttr;
            }).collect(Collectors.toList());
            device.setDeviceAttrList(deviceAttrList);
        }
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductCode, responseDeviceInfo.getProductKey()));
        if (productMap != null) {
            device.setProductId(productMap.getProductId());
        } else {
            log.info("未获取到产品信息：{}", JSON.toJSONString(device));
        }
        device.setStatus(responseDeviceInfo.getStatus());
        device.setSn(responseDeviceInfo.getSn());

        return responseDeviceInfo.getThirdpartyCode();
    }

    @Override
    public boolean delDevice(String deviceId) {
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        ProjectDeviceInfoDto deviceInfoDto = new ProjectDeviceInfoDto();

        if (StrUtil.isEmpty(deviceInfo.getThirdpartyCode())) {
            return true;
        }

        BeanPropertyUtil.copyProperty(deviceInfoDto, deviceInfo, new FieldMapping<ProjectDeviceInfoDto, ProjectDeviceInfoProxyVo>()
                .add(ProjectDeviceInfoDto::getDeviceId, ProjectDeviceInfo::getDeviceId)
                .add(ProjectDeviceInfoDto::getIpv4, ProjectDeviceInfo::getIpv4)
                .add(ProjectDeviceInfoDto::getThirdpartyCode, ProjectDeviceInfo::getThirdpartyCode)
        );
        R<Boolean> response = remoteParkingDeviceService.delDevice(deviceInfoDto);
        return response.getData();
    }

    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device, String productKey) {
        return "";
    }

    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList) {
        return new String[0];
    }

    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList, String productKey) {
        return new String[0];
    }

    @Override
    public boolean updateDevice(ProjectDeviceInfoProxyVo device) {
        return false;
    }

    @Override
    public boolean getDeviceParam(String serviceId, String thirdpartyCode, String deviceId) {
        return false;
    }

    @Override
    public boolean setDeviceParameters(ObjectNode paramNode, String deviceId, String thirdpartyCode) {
        return false;
    }

    @Override
    public boolean getDeviceInfo(String deviceId, String thirdpartyCode) {
        return false;
    }

    @Override
    public boolean reboot(String deviceId) {
        return false;
    }

    @Override
    public boolean callElevator(String deviceId, String roomCode, String floor, String liftDirect) {
        return false;
    }

    @Override
    public boolean reset(String deviceId) {
        return false;
    }

    @Override
    public boolean setPwd(String deviceId, String password) {
        return false;
    }

    @Override
    public boolean syncProduces(int projectId, int tenantId) {
        return false;
    }

    @Override
    public boolean subscribe(String deviceType, int projectId, int tenantId) {
        return false;
    }

    @Override
    public boolean syncDecvice(String deviceType, int projectId, int tenantId) {
        return false;
    }

    @Override
    public boolean dealAlarm(String eventId) {
        return false;
    }

    @Override
    public boolean sendMediaAd(String deviceId, MediaAdInfoVo media) {
        return false;
    }

    @Override
    public boolean cleanMediaAd(Long adSeq, String deviceId) {
        return false;
    }

    @Override
    public boolean setAccount(ProjectDeviceInfoProxyVo device) {
        return false;
    }

    @Override
    public boolean setGatherAlarmRule(ProjectDeviceInfoProxyVo device, ProjectDeviceInfoProxyVo parDevice, ProjectDeviceGatherAlarmRuleVo rule) {
        return false;
    }

    @Override
    public AurineEdgeDeviceInfoDTO getChannel(ProjectDeviceInfoProxyVo deviceInfo) {
        return null;
    }

    /**
     * 梯控操作楼层
     *
     * @param deviceId 设备id
     * @param floors
     * @return
     * @Param floors 楼层
     */
    @Override
    public boolean operateFloor(String deviceId, String[] floors) {
        return true;
    }


    @Override
    public boolean regDevice(DeviceRegDto deviceRegDto) {
        ProjectDeviceInfoProxyVo existDeviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(deviceRegDto.getThirdpartyCode());
        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();
        TenantContextHolder.setTenantId(1);
        if (existDeviceInfo == null) {
            log.info("[车场设备自动注册] 设备不存在，将新增设备");
            BeanPropertyUtil.copyProperty(deviceInfo, deviceRegDto);
            ProjectDeviceRegion publicRegion = deviceRegionService.getPublicRegion();
            deviceInfo.setDeviceRegionId(publicRegion != null ? publicRegion.getRegionId() : "");
            String deviceId = UUID.randomUUID();
            deviceInfo.setDeviceId(deviceId);
            SysDeviceProductMap productMap = sysDeviceProductMapService.getByProductCode(deviceRegDto.getProductCode());
            deviceInfo.setProductId(productMap.getProductId());
            deviceInfo.setDeviceType(deviceRegDto.getDeviceType());
            deviceInfo.setSn(deviceRegDto.getSn());
            deviceInfo.setBrand(productMap.getProductName() + "-" + productMap.getProductModel());
            projectDeviceInfoProxyService.save(deviceInfo);
            String videoUrl = deviceRegDto.getVideoUrl();
            if (StrUtil.isNotEmpty(videoUrl)) {
                deviceAttrService.saveDeviceAttr(deviceId, "videoUrl", "视频URL", videoUrl);
            } else {
                log.info("[车场设备自动注册] 本次自动注册设备没有视频URL：{}", JSON.toJSONString(deviceRegDto));
            }
        } else {
            log.info("[车场设备自动注册] 设备已存在更新设备信息：{}", JSON.toJSONString(deviceRegDto));
            BeanPropertyUtil.copyProperty(deviceInfo, existDeviceInfo).copyProperty(deviceRegDto);
            String videoUrl = deviceRegDto.getVideoUrl();
            if (StrUtil.isNotEmpty(videoUrl)) {
                deviceAttrService.saveDeviceAttr(deviceInfo.getDeviceId(), "videoUrl", "视频URL", videoUrl);
            }
            projectDeviceInfoProxyService.updateById(deviceInfo);
        }
        ProjectDeviceInfoDto deviceInfoDto = new ProjectDeviceInfoDto();
        deviceInfoDto.setThirdpartyCode(deviceInfo.getThirdpartyCode());
        log.info("[车场设备自动注册] 调用车场服务设备激活接口：{}", JSON.toJSONString(deviceInfoDto));
        remoteParkingDeviceService.activeDevice(deviceInfoDto, SecurityConstants.FROM_IN);

        this.configDefaultParam(deviceInfo.getDeviceId());
        return true;
    }

    @Override
    @SneakyThrows
    public boolean configDefaultParam(String deviceId) {
        log.info("[车场设备自动注册] 配置默认参数到车道一体机");
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        ObjectNode param = objectMapper.createObjectNode();

        ParkingDeviceScreenDto screenDto = new ParkingDeviceScreenDto();
        screenDto.setDefaultValidTime("2022-01-01 00:00:00~2099-01-01 00:00:00");
        List<ParkShowDetailDto> defaultDetailList = new ArrayList<>();
        defaultDetailList.add(new ParkShowDetailDto("red", "欢迎光临", 1));
        defaultDetailList.add(new ParkShowDetailDto("red", "", 2));
        screenDto.setDefaultContent(defaultDetailList);

        List<ParkShowDetailDto> unexpiredInDetailList = new ArrayList<>();
        unexpiredInDetailList.add(new ParkShowDetailDto("red", ParkingTextModelEnum.PLATE_NUMBER.getModel(), 1));
        unexpiredInDetailList.add(new ParkShowDetailDto("red", "欢迎回家", 2));
        screenDto.setUnexpiredInSetup(unexpiredInDetailList);

        List<ParkShowDetailDto> outDetailList = new ArrayList<>();
        outDetailList.add(new ParkShowDetailDto("red", ParkingTextModelEnum.PLATE_NUMBER.getModel(), 1));
        outDetailList.add(new ParkShowDetailDto("red", ParkingTextModelEnum.STOP_TIME.getModel(), 2));
        screenDto.setUnexpiredOutSetup(outDetailList);
        screenDto.setExpiredOutSetup(outDetailList);
        screenDto.setUnrecordedOutSetup(outDetailList);


        List<ParkShowDetailDto> expiredInDetailList = new ArrayList<>();
        expiredInDetailList.add(new ParkShowDetailDto("red", ParkingTextModelEnum.PLATE_NUMBER.getModel(), 1));
        expiredInDetailList.add(new ParkShowDetailDto("red", "车辆已到期", 2));
        screenDto.setExpiredInSetup(expiredInDetailList);


        List<ParkShowDetailDto> unrecordedInDetailList = new ArrayList<>();
        unrecordedInDetailList.add(new ParkShowDetailDto("red", ParkingTextModelEnum.PLATE_NUMBER.getModel(), 1));
        unrecordedInDetailList.add(new ParkShowDetailDto("red", "无效车牌", 2));
        screenDto.setUnrecordedInSetup(unrecordedInDetailList);
        param.putPOJO(DeviceParamEnum.PARKING_DEVICE_SCREEN.getServiceId(), screenDto);

        ParkingDeviceVoiceDto voiceDto = new ParkingDeviceVoiceDto();
        voiceDto.setEnable("1");
        voiceDto.setInSetup(new ParkInOutSetupDto("1", "0", "0", "1", "欢迎回家"));
        voiceDto.setOutSetup(new ParkInOutSetupDto("1", "1", "0", "1", "一路顺风"));
        param.putPOJO(DeviceParamEnum.PARKING_DEVICE_VOICE.getServiceId(), voiceDto);

        ParkingDisplaySettingDto displaySettingDto = new ParkShowDetailDto();
        displaySettingDto.setMoveMode("3");
        displaySettingDto.setShowTime("30");
        displaySettingDto.setStopTime("3");
        param.putPOJO("displaySetting", displaySettingDto);

        ParkingOtherSettingDto otherSettingDto = new ParkingOtherSettingDto();
        otherSettingDto.setLEDBrightness(70);
        otherSettingDto.setFillStartTime("18");
        otherSettingDto.setFillEndTime("08");
        List<ParkVolumeSetupDto> volumeSetup = new ArrayList<>();
        volumeSetup.add(new ParkVolumeSetupDto(2, "18", "08"));
        otherSettingDto.setVolumeSetup(volumeSetup);
        param.putPOJO(DeviceParamEnum.PARKING_OTHER_SETTING.getServiceId(), otherSettingDto);

        ParkingParamInfoVo paramInfoVo = new ParkingParamInfoVo();
        paramInfoVo.setDeviceId(deviceId);
        paramInfoVo.setParamJson(param.toString());
        log.info("[车场设备自动注册] 配置车道一体机 {} 的默认参数{}", deviceId, objectMapper.writeValueAsString(paramInfoVo));
        R r = projectDeviceInfoService.setParkingDeviceParam(paramInfoVo);
        return R.ok().getCode() == r.getCode();
    }

    @Override
    public boolean openAlways(String deviceId, Integer doorAction) {
        return false;
    }

    @Override
    public boolean statusChange(DeviceStatusDto dto) {
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(dto.getThirdpartyCode());
        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
        TenantContextHolder.setTenantId(1);
        if (deviceInfo != null) {
            projectDeviceInfoService.update(new LambdaUpdateWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, deviceInfo.getDeviceId()).set(ProjectDeviceInfo::getStatus, dto.getStatus()));
        }
        return false;
    }

}
