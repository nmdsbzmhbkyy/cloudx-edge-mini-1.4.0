package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 华为中台 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
@Slf4j
public class PerimeterAlarmServiceImplByAurineV2_1 implements PerimeterAlarmDeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectInfoService projectInfoService;


    /**
     * 同步通道
     *
     * @param deviceInfo 设备
     * @return
     */
    @Override
    public List<ProjectPerimeterAlarmArea> queryChannel(ProjectDeviceInfo deviceInfo) {
        return new ArrayList<>();
    }

    /**
     * 防区布防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelProtection(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        return true;
    }

    /**
     * 防区撤防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelRemoval(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        return true;
    }

    /**
     * 消除防区告警
     *
     * @param perimeterAlarmArea 防区（通道）
     */
    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        return true;
    }

    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent) {
        return false;
    }

    //@Override
    public boolean queryDevParams(String devId, AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        return false;
    }

    @Override
    public boolean setDevParams(ProjectDeviceInfo deviceInfo, AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        return false;
    }

    //@Override
    public boolean setDevParams(ProjectDeviceInfo deviceInfo) {
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
