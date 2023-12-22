package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.dto.DeviceRegDto;
import com.aurine.cloudx.estate.dto.DeviceStatusDto;
import com.aurine.cloudx.estate.entity.SysThirdPartyInterfaceConfig;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote.factory.AurineRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 冠林中台 V1 版本 对接业务实现
 *
 * @ClassName: PassWayDeviceServiceImplByAurineV1
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:00
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class DeviceServiceImplByAurineV2_1 implements DeviceService {

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private SysThirdPartyInterfaceConfigService sysThirdPartyInterfaceConfigService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    /**
     * 订阅消息
     *
     * @return
     */
    @Override
    public boolean subscribe(String deviceType, int projectId, int tenantId) {
        log.info("开始订阅");
        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId, AurineConfigDTO.class);
        boolean result = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).subscribe(config);

        if (result) {
            SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId);
            sysThirdPartyInterfaceConfig.setSubscribeStatus("1");
            sysThirdPartyInterfaceConfigService.updateById(sysThirdPartyInterfaceConfig);
        }

        return true;
    }


    /***
     * 同步设备
     * @return
     */
    @Override
    public boolean syncDecvice(String deviceType, int projectId, int tenantId) {
        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId, AurineConfigDTO.class);
        JSONObject result = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).syncDevice(config, 0, 0);
        return false;
    }

    @Override
    public boolean dealAlarm(String eventId) {
        return false;
    }

    /**
     * 新增设备
     *
     * @param deviceInfo
     * @return
     */
    @Override
    public String addDevice(ProjectDeviceInfoProxyVo deviceInfo) {

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //获取对接配置
            AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), AurineConfigDTO.class);
            //调用对接业务
            AurineDeviceVo vo = AurineUtil.createDeviceVo(deviceInfo, "");
            String thirdCode = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).addDevice(config, vo);

            //对设备所在楼栋、单元、房屋添加框架号 设备号不是框架号，取消该功能
            if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode()) && StringUtils.isNotEmpty(thirdCode)) {
                log.info("设备返回第三方编号{}，开始对楼栋单元房屋进行添加运算", thirdCode);
                projectBuildingInfoService.addThirdCode(deviceInfo, thirdCode);
            }

            return thirdCode;
        } else {
            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
        }
    }

    @Override
    public String addDevice(ProjectDeviceInfoProxyVo device, String productKey) {
        return this.addDevice(device);
    }

    /**
     * 批量新增设备
     *
     * @param deviceList
     * @return
     */
    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList) {
        String[] result = new String[deviceList.size()];

        if (CollUtil.isNotEmpty(deviceList)) {
            for (int i = 0; i < deviceList.size(); i++) {
                result[i] = this.addDevice(deviceList.get(i));
            }
        }

        return result;
    }

    @Override
    public String[] addDeviceBatch(List<ProjectDeviceInfoProxyVo> deviceList, String productKey) {
        return this.addDeviceBatch(deviceList);
    }

    /**
     * 修改设备
     *
     * @param device
     * @return
     */
    @Override
    public boolean updateDevice(ProjectDeviceInfoProxyVo device) {
        return true;
    }

    @Override
    public boolean getDeviceParam(String serviceId, String thirdpartyCode, String deviceId) {
        return false;
    }

    @Override
    public boolean setDeviceParameters(ObjectNode paramNode, String deviceId, String thirdpartyCode) {
        return true;
    }

    @Override
    public boolean getDeviceInfo(String deviceId, String thirdpartyCode) {
        return true;
    }


    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean delDevice(String deviceId) {
        ProjectDeviceInfoVo projectDeviceInfoVo = projectDeviceInfoService.getProjectDeviceInfoById(deviceId);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //获取对接配置
            AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineConfigDTO.class);
            //调用对接业务
            AurineDeviceVo vo = new AurineDeviceVo();
            vo.setCommunityId(deviceInfo.getProjectId());
            vo.setDevsn(deviceInfo.getSn());
            vo.setMac(deviceInfo.getMac());
            vo.setName(deviceInfo.getDeviceName());

            return AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).delDevice(config, vo);
        } else {
            return true;
        }
    }

    /**
     * 重启
     *
     * @param deviceId 设备id
     * @return
     */
    @Override
    public boolean reboot(String deviceId) {
        //Aurine中台的重启业务处理
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineConfigDTO.class);

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //调用对接业务
            return AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).reboot(config, deviceInfo.getSn());
        } else {
            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
        }
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

    /**
     * 同步产品
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean syncProduces(int projectId, int tenantId) {
        return true;
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
    public boolean setGatherAlarmRule(ProjectDeviceInfoProxyVo device, ProjectDeviceInfoProxyVo ParDevice, ProjectDeviceGatherAlarmRuleVo rule) {
        return false;
    }

    @Override
    public AurineEdgeDeviceInfoDTO getChannel(ProjectDeviceInfoProxyVo deviceInfo) {
        return null;
    }

    @Override
    public boolean operateFloor(String deviceId, String[] floors) {
        return false;
    }

    @Override
    public boolean regDevice(DeviceRegDto dto) {
        return false;
    }

    @Override
    public boolean statusChange(DeviceStatusDto dto) {
        return false;
    }

    @Override
    public boolean configDefaultParam(String deviceId) {
        return false;
    }

    @Override
    public boolean openAlways(String deviceId, Integer doorAction) {
        return false;
    }


    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V2_1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_MIDDLE.code;
    }

}
