package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.factory.AliRemotePerimeterAlarmServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class PerimeterAlarmServiceImplByHuaweiV1 implements PerimeterAlarmDeviceService {

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
        List<ProjectPerimeterAlarmArea> resultList = new ArrayList<>();
        //获取通道信息
        JSONObject respJson = AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer()).channelListQuery(deviceInfo.getSn(), deviceInfo.getProjectId());


        JSONArray jsonArray = respJson.getJSONArray("body");
        if (jsonArray == null || jsonArray.size() == 0){
            return resultList;
        }

        for (String channelNo : jsonArray.toJavaList(String.class)) {
            ProjectPerimeterAlarmArea alarmArea = new ProjectPerimeterAlarmArea();
            alarmArea.setChannelNo(channelNo);
            alarmArea.setDeviceId(deviceInfo.getDeviceId());
            alarmArea.setArmedStatus("0");//默认给与撤防状态

            resultList.add(alarmArea);
        }


        return resultList;
    }

    /**
     * 防区布防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelProtection(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        JSONObject respJson = AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer())
                .channelOperate(deviceInfo.getSn(), perimeterAlarmArea.getChannelNo(), "1", deviceInfo.getProjectId());

        return handleResp(respJson);
    }

    /**
     * 防区撤防
     *
     * @param perimeterAlarmArea 防区（通道）
     * @return
     */
    @Override
    public boolean channelRemoval(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        JSONObject respJson = AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer())
                .channelOperate(deviceInfo.getSn(), perimeterAlarmArea.getChannelNo(), "0", deviceInfo.getProjectId());

        return handleResp(respJson);
    }

    /**
     * 消除防区告警
     *
     * @param perimeterAlarmArea 防区（通道）
     */
    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmArea perimeterAlarmArea) {
        //获取主机信息
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getProjectDeviceInfoById(perimeterAlarmArea.getDeviceId());
        JSONObject respJson = AliRemotePerimeterAlarmServiceFactory.getInstance(this.getVer())
                .clearAlarm(deviceInfo.getSn(), perimeterAlarmArea.getChannelNo(), deviceInfo.getProjectId());

        return handleResp(respJson);
    }

    @Override
    public boolean clearAlarm(ProjectPerimeterAlarmEvent projectPerimeterAlarmEvent) {
        return false;
    }

    @Override
    public boolean queryDevParams(String devId, AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        return false;
    }

    @Override
    public boolean setDevParams(ProjectDeviceInfo deviceInfo, AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO) {
        return false;
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

    /**
     * 获取版本
     *
     * @return
     */
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.HUAWEI_MIDDLE.code;
    }


    private boolean handleResp(JSONObject json) {
//        {"errorCode":1,"errorMsg":"操作失败"}
        String errorCode = json.getString("errorCode");
        if (StringUtils.equals("0", errorCode)) {
            return true;
        } else {
            String errorMsg = json.getString("errorMsg");
            log.error("[阿里边缘][周界报警] 操作失败：{}", json);
            return false;
        }

    }

}
