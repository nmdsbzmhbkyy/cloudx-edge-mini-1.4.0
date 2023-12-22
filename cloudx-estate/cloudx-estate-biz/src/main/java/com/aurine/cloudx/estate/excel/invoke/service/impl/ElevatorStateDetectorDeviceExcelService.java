package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.excel.entity.access.ElevatorStateDetectorDeviceExcel;
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
import java.util.HashSet;
import java.util.Set;

/**
 * <p>电梯分层控制器导入</p>
 *
 * @author : hjj
 * @date : 2022-09-20 09:53:29
 */
@Service
public class ElevatorStateDetectorDeviceExcelService extends BaseExcelRowInvokeService<ElevatorStateDetectorDeviceExcel> {

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
    private ProjectDeviceRegionService projectDeviceRegionService;

    HashSet<String> deviceSnSet = new HashSet<>();

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, ElevatorStateDetectorDeviceExcel data, AnalysisContext context, boolean enablePolice) {

        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        BeanUtils.copyProperties(data, deviceInfo);

        //校验区域
        String regionId = projectDeviceRegionService.getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, data.getDeviceRegionName().replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            return RowInvokeResult.failed("区域填写错误");
        }

        //电梯状态检测器sn必填
        if (StrUtil.equals(DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE, deviceInfo.getDeviceType())) {
            if (StrUtil.isBlank(deviceInfo.getSn())) {
                return RowInvokeResult.failed("sn未填写");
            }
        }

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
                        return RowInvokeResult.failed("未找到该单元");
                    }
                }
            } else {
                return RowInvokeResult.failed("未找到该楼栋");
            }
        }

        // 已对格式进行校验
        if (StrUtil.isNotEmpty(data.getPort())) {
            deviceInfo.setPort(Integer.parseInt(data.getPort()));
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

        return RowInvokeResult.success(deviceInfo);
    }

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, ElevatorStateDetectorDeviceExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        return null;
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE);
        return deviceTypeSet;
    }

    @Override
    public Set<String> getRedisKeys(String batchId) {
        return null;
    }

    @Override
    public void clearCache() {
        deviceSnSet.clear();
    }
}
