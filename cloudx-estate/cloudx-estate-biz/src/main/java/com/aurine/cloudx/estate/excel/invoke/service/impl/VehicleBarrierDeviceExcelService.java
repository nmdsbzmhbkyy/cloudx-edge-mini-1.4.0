package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.excel.entity.access.VehicleBarrierDeviceExcel;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceProductNameVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
* @Author hjj
* @Description 车辆道闸一体机导入
* @Date  2022/8/11
* @Param
* @return
**/
@Service
public class VehicleBarrierDeviceExcelService extends BaseExcelRowInvokeService<VehicleBarrierDeviceExcel> {

    private static final String DEVICE_REGION_KEY_PRE = "device_region_";

    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    HashSet<String> deviceSnSet = new HashSet<>();

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, VehicleBarrierDeviceExcel data, AnalysisContext context, boolean enablePolice) {

        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        BeanUtils.copyProperties(data, deviceInfo);

        //校验区域
        String regionId = projectDeviceRegionService.getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, data.getDeviceRegionName().replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            return RowInvokeResult.failed("区域填写错误");
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
        //新增时暂时设置状态为未激活状态
        deviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
        //设置为手动接入
        deviceInfo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);
        return RowInvokeResult.success(deviceInfo);
    }

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, VehicleBarrierDeviceExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        return null;
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.VEHICLE_BARRIER_DEVICE);
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
