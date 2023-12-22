package com.aurine.cloudx.estate.excel.invoke.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.AttrsConfig;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.excel.entity.access.GateAndCenterDeviceExcel;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>区口机和中心机导入解析</p>
 *
 * @author : 王良俊
 * @date : 2021-09-14 09:51:40
 */
@Service
public class GateAndCenterDeviceExcelService extends ExcelVoAttrsHandleService<GateAndCenterDeviceExcel> {

    @Resource
    ProjectFrameInfoService projectFrameInfoService;
    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;

    private static final String DEVICE_REGION_KEY_PRE = "device_region_";
    private static final String DEVICE_FRAME_KEY_PRE = "device_frame_";

    HashSet<String> deviceSnSet = new HashSet<>();
    private List<AttrsConfig> attrsConfigs=new ArrayList<>();

    @Override
    public void customValid(ProjectDeviceInfoVo deviceInfo,String batchId, GateAndCenterDeviceExcel data, AnalysisContext context, boolean enablePolice) {
//        if(StrUtil.isNotEmpty(data.getDeviceEntityName())) {
//            String frameIdByName = projectFrameInfoService.getFrameIdByName(DEVICE_FRAME_KEY_PRE + batchId, data.getDeviceEntityName().replaceAll(" ", ""));
//            if (StrUtil.isNotEmpty(frameIdByName)) {
//                deviceInfo.setDeviceEntityId(frameIdByName);
//            } else {
//                return RowInvokeResult.failed("组团填写错误");
//            }
//        }
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
    public RowInvokeResult excelRowInvoke(String batchId, GateAndCenterDeviceExcel data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        return null;
    }

    @Override
    public Set<String> getDeviceTypeSet() {
        Set<String> deviceTypeSet = new HashSet<>();
        deviceTypeSet.add(DeviceTypeConstants.GATE_DEVICE);
        deviceTypeSet.add(DeviceTypeConstants.CENTER_DEVICE);
        return deviceTypeSet;
    }

    @Override
    public Set<String> getRedisKeys(String batchId) {
        Set<String> keys = new HashSet<>();
        keys.add(DEVICE_REGION_KEY_PRE + batchId);
        keys.add(DEVICE_FRAME_KEY_PRE + batchId);
        return keys;
    }

    @Override
    public void clearCache() {
        deviceSnSet.clear();
    }
}
