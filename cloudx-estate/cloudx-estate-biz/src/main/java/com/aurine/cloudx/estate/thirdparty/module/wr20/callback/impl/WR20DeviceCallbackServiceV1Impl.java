package com.aurine.cloudx.estate.thirdparty.module.wr20.callback.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20DeviceCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20DeviceObj;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * WR20 设备回调接口
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-17 14:04
 * @Copyright:
 */
@Service
@Slf4j
public class WR20DeviceCallbackServiceV1Impl implements WR20DeviceCallbackService {
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectDockModuleConfService projectDockModuleConfService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    /**
     * 同步设备
     *
     * @param jsonObject
     * @param gatewayId
     * @param deviceType 1:区口机 2：梯口机 4：室内机 0：全部
     * @return
     */
    @Override
    public void syncDevice(JSONObject jsonObject, String deviceType, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        log.info("[WR20] 开始同步项目：{} 的 设备数据 {}", wr20Config.getProjectId(), jsonObject);
        List<WR20DeviceObj> deviceObjList = jsonObject.getJSONArray("list").toJavaList(WR20DeviceObj.class);
        this.syncDeviceList(deviceObjList, deviceType, wr20Config.getProjectId());

        log.info("[WR20] 同步设备 类型：{} 流程结束{}", deviceType, gatewayId);
        ProjectContextHolder.clear();
    }


    /**
     * sync device list by type
     *
     * @param deviceType
     * @para deviceObjList
     */
    private void syncDeviceList(List<WR20DeviceObj> deviceObjList, String deviceType, int projectId) {
        String deviceTypeCode = "";
        //同步区口机
        if (StringUtils.equals("1", deviceType)) {
            deviceTypeCode = DeviceTypeEnum.GATE_DEVICE.getCode();
            //同步梯口机
        } else if (StringUtils.equals("2", deviceType)) {
            deviceTypeCode = DeviceTypeEnum.LADDER_WAY_DEVICE.getCode();
        } else {//同步室内机
            deviceTypeCode = DeviceTypeEnum.INDOOR_DEVICE.getCode();
        }

        ProjectDeviceInfoProxyVo deviceInfoProxyVo;
        String deviceNo = "";
        String frameNo = "";
        ProjectFrameInfo house = null;
        ProjectFrameInfo unit = null;
        ProjectBuildingInfoVo building = null;
        for (WR20DeviceObj wr20DeviceObj : deviceObjList) {

            deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
            deviceInfoProxyVo.setDeviceId(UUID.randomUUID().toString().replaceAll("-", ""));
            deviceInfoProxyVo.setSn(wr20DeviceObj.getDevSN());
            deviceInfoProxyVo.setDeviceType(deviceTypeCode);
            deviceInfoProxyVo.setDeviceName(wr20DeviceObj.getName());
//            deviceInfoProxyVo.setThirdpartyCode(wr20DeviceObj.getDeviceNo());
//            deviceInfoProxyVo.setDeviceCode(wr20DeviceObj.getDevId());
            deviceInfoProxyVo.setThirdpartyCode(wr20DeviceObj.getDevId());
//            deviceInfoProxyVo.setDeviceCode(wr20DeviceObj.getDeviceNo());
            deviceInfoProxyVo.setThirdpartNo(wr20DeviceObj.getDeviceNo());

            //获取设备所在框架号，并进行绑定
//            deviceNo = wr20DeviceObj.getDeviceNo();
//            frameNo = deviceNo.substring(deviceNo.indexOf("_") + 1, deviceNo.length() - 1);
            frameNo = wr20DeviceObj.getFrameNo();

            //通过框架号获取设备所在的位置
            //区口机
            if (StringUtils.equals("1", deviceType)) {
                //区口机不设组团，默认为公共
                //梯口机
            } else if (StringUtils.equals("2", deviceType)) {
                unit = projectFrameInfoService.getByFrameNo(frameNo);

                if (unit != null) {
                    building = projectBuildingInfoService.getById(unit.getPuid());
                    deviceInfoProxyVo.setBuildingId(building.getBuildingId());
                    deviceInfoProxyVo.setUnitId(unit.getEntityId());
                    deviceInfoProxyVo.setDeviceRegionId(building.getRegionId());
                }

            } else {//室内机
                house = projectFrameInfoService.getByFrameNo(frameNo);
                if (house != null) {
                    unit = projectFrameInfoService.getParent(house.getEntityId());
                    building = projectBuildingInfoService.getById(unit.getPuid());

                    deviceInfoProxyVo.setBuildingId(building.getPuid());
                    deviceInfoProxyVo.setUnitId(unit.getEntityId());
                    deviceInfoProxyVo.setHouseId(house.getEntityId());
                    deviceInfoProxyVo.setDeviceRegionId(building.getRegionId());
                }
            }

            deviceInfoProxyVo.setLat(StringUtils.isNumeric(wr20DeviceObj.getGisInfo().getLatiude()) ? new BigDecimal(Double.valueOf(wr20DeviceObj.getGisInfo().getLatiude())) : null);
            deviceInfoProxyVo.setAlt(StringUtils.isNumeric(wr20DeviceObj.getGisInfo().getAlt()) ? new BigDecimal(Double.valueOf(wr20DeviceObj.getGisInfo().getAlt())) : null);
            deviceInfoProxyVo.setLon(StringUtils.isNumeric(wr20DeviceObj.getGisInfo().getLongitude()) ? new BigDecimal(Double.valueOf(wr20DeviceObj.getGisInfo().getLongitude())) : null);

            //如果未能从楼栋中获取区域配置，则获取当前项目的默认区域(或首个区域)
            if (StringUtils.isEmpty(deviceInfoProxyVo.getDeviceRegionId())) {
                List<ProjectDeviceRegion> regionList = projectDeviceRegionService.list(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getParRegionId, "1"));
                if (CollUtil.isNotEmpty(regionList)) {
                    deviceInfoProxyVo.setDeviceRegionId(regionList.get(0).getRegionId());
                }
            }

            deviceInfoProxyVo.setProjectId(projectId);
            deviceInfoProxyVo.setTenantId(1);
            deviceInfoProxyVo.setStatus("1");
            deviceInfoProxyVo.setActive("1");
            deviceInfoProxyVo.setCloudCtl("0");

            projectDeviceInfoProxyService.saveOrUpdateDeviceByThirdPartyCode(deviceInfoProxyVo);
        }
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

}
