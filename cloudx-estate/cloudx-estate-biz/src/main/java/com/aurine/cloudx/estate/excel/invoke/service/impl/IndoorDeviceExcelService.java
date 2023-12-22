package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.excel.entity.access.IndoorDeviceExcel;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.ObjectPaseUtils;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceProductNameVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>梯口机导入</p>
 *
 * @author : 王良俊
 * @date : 2021-09-14 09:51:40
 */
@Service
public class IndoorDeviceExcelService extends BaseExcelRowInvokeService<IndoorDeviceExcel> {

    private static final String DEVICE_REGION_KEY_PRE = "device_region_";
    @Resource
    ProjectFrameInfoService projectFrameInfoService;
    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;
    @Resource
    ProjectDeviceRegionService projectDeviceRegionService;

    HashSet<String> deviceSnSet = new HashSet<>();

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, IndoorDeviceExcel data, AnalysisContext context, boolean enablePolice) {

        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        if(StrUtil.isNotBlank(data.getDeviceCode())){
            ProjectDeviceInfo codeDevice = projectDeviceInfoService.getOne(new LambdaQueryWrapper<>(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getDeviceCode, data.getDeviceCode()));
            if(codeDevice != null){
                return RowInvokeResult.failed("设备编号已存在");
            }
        }
        BeanUtils.copyProperties(data, deviceInfo);


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

        /*所属区域*/
        String regionId = projectDeviceRegionService.getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, data.getDeviceRegionName().replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            return RowInvokeResult.failed("区域填写错误");
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

        /**
         * 扩展属性
         */
        List<ProjectDeviceAttrListVo> projectDeviceAttrListVos = this.projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(),deviceLoadLog.getDeviceType(),null);
        if(projectDeviceAttrListVos != null){
            for (ProjectDeviceAttrListVo projectDeviceAttrListVo:
                    projectDeviceAttrListVos) {
                Object value = ObjectPaseUtils.getFieldValueByName(projectDeviceAttrListVo.getAttrCode(),data);
                if(value != null){
                    projectDeviceAttrListVo.setAttrValue(value.toString());
                }
            }
        }
        deviceInfo.setDeviceAttrs(projectDeviceAttrListVos);
        //新增时暂时设置状态为未激活状态
        deviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
        //设置为手动接入
        deviceInfo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);
        return RowInvokeResult.success(deviceInfo);
    }

    @Override
    public RowInvokeResult excelRowInvoke(String batchId, IndoorDeviceExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        BeanUtils.copyProperties(data, deviceInfo);
        if(StrUtil.isNotBlank(data.getDeviceCode())){
            ProjectDeviceInfo codeDevice = projectDeviceInfoService.getOne(new LambdaQueryWrapper<>(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getDeviceCode, data.getDeviceCode()));
            if(codeDevice != null){
                if(StrUtil.equals("1", codeDevice.getConfigured())){
                    return RowInvokeResult.failed("设备已激活");
                }else {
                    projectDeviceInfoService.removeDevice(codeDevice.getDeviceId());
                }
            }
        }



        // 校验设备SN是否重复
        if (StrUtil.isNotEmpty(data.getSn())) {
            ProjectDeviceInfoProxyVo byDeviceSn = projectDeviceInfoProxyService.getByDeviceSn(data.getSn());
            if (byDeviceSn != null && !StrUtil.equals(deviceInfo.getDeviceId(), byDeviceSn.getDeviceId())) {
                deviceSnSet.add(data.getSn());
                return RowInvokeResult.failed("设备SN重复");
            }
        }

        /*所属区域*/
        String regionId = projectDeviceRegionService.getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, data.getDeviceRegionName().replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            return RowInvokeResult.failed("区域填写错误");
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

        /**
         * 扩展属性
         */
        List<ProjectDeviceAttrListVo> projectDeviceAttrListVos = this.projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(),deviceLoadLog.getDeviceType(),null);
        if(projectDeviceAttrListVos != null){
            for (ProjectDeviceAttrListVo projectDeviceAttrListVo:
                    projectDeviceAttrListVos) {
                Object value = ObjectPaseUtils.getFieldValueByName(projectDeviceAttrListVo.getAttrCode(),data);
                if(value != null){
                    projectDeviceAttrListVo.setAttrValue(value.toString());
                }
            }
        }
        deviceInfo.setDeviceAttrs(projectDeviceAttrListVos);
        //新增时暂时设置状态为未激活状态
        deviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
        //设置为手动接入
        deviceInfo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);
        return RowInvokeResult.success(deviceInfo);
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.INDOOR_DEVICE);
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
