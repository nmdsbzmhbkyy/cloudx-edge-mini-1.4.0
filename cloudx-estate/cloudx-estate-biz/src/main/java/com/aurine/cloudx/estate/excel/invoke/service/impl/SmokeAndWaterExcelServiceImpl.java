package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.excel.entity.iot.SmokeAndWaterExcel;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceProductNameVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>烟感和水表的Excel解析实现类</p>
 *
 * @author : 王良俊
 * @date : 2021-09-02 10:54:18
 */
@Service
public class SmokeAndWaterExcelServiceImpl extends BaseExcelRowInvokeService<SmokeAndWaterExcel> {

    @Resource
    ProjectFrameInfoService projectFrameInfoService;

    @Resource
    ProjectDeviceRegionService projectDeviceRegionService;

    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    HashSet<String> deviceSnSet = new HashSet<>();

    private static final String DEVICE_REGION_KEY_PRE = "device_region_";

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, SmokeAndWaterExcel data, AnalysisContext context, boolean enablePolice) {

        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        BeanUtils.copyProperties(data, deviceInfo);
        deviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);

        // 校验设备SN是否重复
        if (StrUtil.isNotEmpty(data.getSn())) {
            if (deviceSnSet.contains(data.getSn())) {
                return RowInvokeResult.failed("设备SN重复");
            } else {
                ProjectDeviceInfoProxyVo byDeviceSn = projectDeviceInfoProxyService.getByDeviceSn(data.getSn());
                if (byDeviceSn != null) {
                    deviceSnSet.add(data.getSn());
                    return RowInvokeResult.failed("设备SN重复");
                }
            }
            deviceSnSet.add(data.getSn());
        }

        String regionId = projectDeviceRegionService.getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, data.getDeviceRegionName().replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            return RowInvokeResult.failed("区域填写错误");
        }

        /*
         * 楼栋、单元、房屋
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
                        if (StrUtil.isNotEmpty(data.getHouse())) {
                            ProjectFrameInfo houseInfo = projectFrameInfoService.getOne(new LambdaQueryWrapper<>(ProjectFrameInfo.class)
                                    .eq(ProjectFrameInfo::getPuid, unitInfo.getEntityId())
                                    .eq(ProjectFrameInfo::getEntityName, data.getHouse())
                                    .eq(ProjectFrameInfo::getIsHouse, "1"));
                            if (houseInfo != null) {
                                deviceInfo.setHouseId(houseInfo.getEntityId());
                            } else {
                                return RowInvokeResult.failed("未找到该房屋");
                            }
                        }
                    } else {
                        return RowInvokeResult.failed("未找到该单元");
                    }
                }
            } else {
                return RowInvokeResult.failed("未找到该楼栋");
            }
        }


        Integer projectId = projectDeviceInfoProxyService.countSn(data.getSn(), "");

        if (projectId != null) {
            if (projectId.equals(ProjectContextHolder.getProjectId())) {
                return new RowInvokeResult(true, "设备SN已存在");
            } else {
                return new RowInvokeResult(true, "设备已占用");
            }
        }

        /**
         * 品牌型号
         */
        ProjectDeviceLoadLog deviceLoadLog = projectDeviceLoadLogService.getOne(Wrappers.lambdaQuery(ProjectDeviceLoadLog.class)
                .eq(ProjectDeviceLoadLog::getBatchId, batchId));
        if (StrUtil.isNotEmpty(data.getBrand())) {
            ProjectDeviceProductNameVo deviceBrand = projectDeviceInfoService.getDeviceBrand(deviceLoadLog.getDeviceType(), data.getBrand());
            if (deviceBrand != null) {
                deviceInfo.setProductId(deviceBrand.getProductId());
            } else {
                return RowInvokeResult.failed("请填写正确的品牌型号");
            }
        }

        // 已对格式进行校验
        if (StrUtil.isNotEmpty(data.getPort())) {
            deviceInfo.setPort(Integer.parseInt(data.getPort()));
        }

        return RowInvokeResult.success(deviceInfo);
    }

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, SmokeAndWaterExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        return null;
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.SMOKE);
        deviceTypeSet.add(DeviceTypeConstants.SMART_WATER_METER);
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
