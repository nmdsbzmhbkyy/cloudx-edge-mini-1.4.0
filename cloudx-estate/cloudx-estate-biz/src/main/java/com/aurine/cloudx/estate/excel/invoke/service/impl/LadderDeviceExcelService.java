package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.AttrsConfig;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.excel.entity.access.LadderDeviceExcel;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceProductNameVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>梯口机导入</p>
 *
 * @author : 王良俊
 * @date : 2021-09-14 09:51:40
 */
@Service
public class LadderDeviceExcelService extends ExcelVoAttrsHandleService<LadderDeviceExcel> {

    private static final String DEVICE_REGION_KEY_PRE = "device_region_";

    @Resource
    ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    ProjectDeviceRegionService projectDeviceRegionService;

    HashSet<String> deviceSnSet = new HashSet<>();
    private List<AttrsConfig> attrsConfigs=new ArrayList<>();
    @Override
    public void customValid(ProjectDeviceInfoVo deviceInfo,String batchId, LadderDeviceExcel data, AnalysisContext context, boolean enablePolice) {
        /*
         * 楼栋、单元
         * */
        if (StrUtil.isNotEmpty(data.getBuilding())) {
            String buildingId = projectFrameInfoService.getBuildingId(data.getBuilding());
            if (StrUtil.isNotEmpty(buildingId)) {
                deviceInfo.setBuildingId(buildingId);
                if (StrUtil.isNotEmpty(data.getUnit())) {
                    ProjectFrameInfo unitInfo = projectFrameInfoService.getOne(new LambdaQueryWrapper<>(ProjectFrameInfo.class)
                            .eq(ProjectFrameInfo::getPuid, buildingId)
                            .eq(ProjectFrameInfo::getEntityName, data.getUnit())
                            .eq(ProjectFrameInfo::getIsUnit, "1"));
                    if (unitInfo != null) {
                        deviceInfo.setUnitId(unitInfo.getEntityId());
                    } else {
                        throw new RuntimeException("未找到该单元");
                    }
                }
            } else {
                throw new RuntimeException("未找到该楼栋");
            }
        }
    }

    @Override
    ProjectDeviceRegionService getRegionService() {
        return projectDeviceRegionService;
    }

    @Override
    ProjectDeviceInfoProxyService getDeviceInfoProxyService() {
        return projectDeviceInfoProxyService;
    }

    @Override
    ProjectDeviceInfoService getDeviceInfoService() {
        return projectDeviceInfoService;
    }

    @Override
    ProjectDeviceLoadLogService getDeviceLoadLogService() {
        return projectDeviceLoadLogService;
    }

    @Override
    void addSn(String sn) {
        this.deviceSnSet.add(sn);
    }

    @Override
    HashSet<String> getSnSet() {
        return this.deviceSnSet;
    }

//    @Override
//    List<AttrsConfig> getAttrConfigs() {
//        return this.attrsConfigs;
//    }

    @Override
    List<AttrsConfig> getAttrConfigsByModel(String model) {
        if(StringUtil.isNotBlank(model)){
            this.attrsConfigs=new ArrayList<>();
            DoorControllerEnum doorControllerEnum = DoorControllerEnum.getByModel(model);
            if(Objects.nonNull(doorControllerEnum)){
                this.attrsConfigs.addAll(doorControllerEnum.getAttrConfs());
            }
        }
        return this.attrsConfigs;
    }

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, LadderDeviceExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        return null;
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.LADDER_WAY_DEVICE);
        return deviceTypeSet;
    }

    @Override
    public Set<String> getRedisKeys(String batchId) {
        Set<String> keys = new HashSet<>();
        keys.add(DEVICE_REGION_KEY_PRE + batchId);
        return keys;
    }

    @Override
    public void clearCache() {
        deviceSnSet.clear();
    }
}
