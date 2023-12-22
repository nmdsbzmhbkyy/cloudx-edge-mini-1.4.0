package com.aurine.cloudx.estate.excel.invoke.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.common.core.constant.enums.AttrsConfig;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceLoadLogService;
import com.aurine.cloudx.estate.service.ProjectDeviceRegionService;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceProductNameVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/***
 * @title ExcelVoAttrsHandleService 处理excel对象实例模板类
 * @description
 * @author cyw
 * @version 1.0.0
 * @create 2023/4/27 17:31
 **/
public abstract class ExcelVoAttrsHandleService<T> extends BaseExcelRowInvokeService<T> {
    private static final String DEVICE_REGION_KEY_PRE = "device_region_";

    /**
     * @param batchId      导入批次ID
     * @param data         excel行数据
     * @param context      easyExcel自带分析对象
     * @param enablePolice 是否开启公安
     * @return com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult
     * @description <excel行数据处理函数>
     * 1、ProjectDeviceInfoVo对象赋值
     * 2、执行公共校验函数
     * 3、执行子类自定义校验函数
     * @author cyw
     * @time 2023/4/28 15:55
     */
    @Override
    public RowInvokeResult excelRowInvoke(String batchId, T data, AnalysisContext context, boolean enablePolice) {

        try {
            ProjectDeviceInfoVo deviceInfo = handleDoorControllerAttrs(data);
            validRow(data, deviceInfo, batchId);
            customValid(deviceInfo, batchId, data, context, enablePolice);
            return RowInvokeResult.success(deviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return RowInvokeResult.failed(e.getMessage());
        }
    }

    /**
     * @param data       excel行数据
     * @param deviceInfo 引用传递->设备vo
     * @param batchId    导入批次ID
     * @description <公共校验函数 对sn port brand deviceRegionName进行校验>
     * @author cyw
     * @time 2023/4/28 15:50
     */
    protected void validRow(T data, ProjectDeviceInfoVo deviceInfo, String batchId) {
        String sn = findValue(data, "sn");
        String port = findValue(data, "port");
        String brand = findValue(data, "brand");
        String deviceRegionName = findValue(data, "deviceRegionName");
        DoorControllerEnum doorController = DoorControllerEnum.getByModel(brand);
        // 校验设备SN是否重复
        if ( StrUtil.isNotEmpty(sn)) {
            if(Objects.isNull(doorController)){
                if (getSnSet().contains(sn)) {
                    throw new RuntimeException("设备SN重复");
                } else {
                    ProjectDeviceInfoProxyVo byDeviceSn = getDeviceInfoProxyService().getByDeviceSn(sn);
                    if (byDeviceSn != null) {
                        throw new RuntimeException("设备SN重复");
                    }
                }
            }
            addSn(sn);
        }

        String regionId = getRegionService().getRegionIdByName(DEVICE_REGION_KEY_PRE + batchId, deviceRegionName.replaceAll(" ", ""));

        if (StrUtil.isNotEmpty(regionId)) {
            deviceInfo.setDeviceRegionId(regionId);
        } else {
            throw new RuntimeException("区域填写错误");
        }
        // 已对格式进行校验
        if (StrUtil.isNotEmpty(port)) {
            deviceInfo.setPort(Integer.parseInt(port));
        }

        // 品牌型号
        ProjectDeviceLoadLog deviceLoadLog = getDeviceLoadLogService().getOne(Wrappers.lambdaQuery(ProjectDeviceLoadLog.class)
                .eq(ProjectDeviceLoadLog::getBatchId, batchId));
        if (StrUtil.isNotEmpty(brand)) {
            ProjectDeviceProductNameVo deviceBrand = getDeviceInfoService().getDeviceBrand(deviceLoadLog.getDeviceType(), brand);
            if (deviceBrand != null) {
                deviceInfo.setProductId(deviceBrand.getProductId());
            } else {
                throw new RuntimeException("请填写正确的品牌型号");
            }
        }
    }

    /**
     * @param data      excel行数据对象
     * @param fieldName 字段名
     * @return java.lang.String
     * @description <根据实例对象及字段名获取属性值>
     * @author cyw
     * @time 2023/4/28 16:10
     */
    public String findValue(T data, String fieldName) {
        try {
            Class<?> clazz = data.getClass();
            Field field = ReflectionUtils.findField(clazz, fieldName);
            ReflectionUtils.makeAccessible(Objects.requireNonNull(field));
            return (String) field.get(data);
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s值解析异常", fieldName));
        }
    }

    abstract void customValid(ProjectDeviceInfoVo deviceInfo, String batchId, T data, AnalysisContext context, boolean enablePolice);

    abstract ProjectDeviceRegionService getRegionService();

    abstract ProjectDeviceInfoProxyService getDeviceInfoProxyService();

    abstract ProjectDeviceInfoService getDeviceInfoService();

    abstract ProjectDeviceLoadLogService getDeviceLoadLogService();

    abstract void addSn(String sn);

    abstract HashSet<String> getSnSet();

    //    abstract List<AttrsConfig> getAttrConfigs();
    abstract List<AttrsConfig> getAttrConfigsByModel(String model);

    /**
     * @param data excel行数据
     * @return com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo
     * @description <处理行数据 并且将拓展属性回填入deviceAttrs中>
     * @author cyw
     * @time 2023/4/28 16:09
     */
    private ProjectDeviceInfoVo handleDoorControllerAttrs(T data) {
        ProjectDeviceInfoVo deviceInfo = new ProjectDeviceInfoVo();
        BeanUtils.copyProperties(data, deviceInfo);

        //根据设备型号brand 判断是否需要通过反射添加拓展字段
        List<ProjectDeviceAttrListVo> attrListVos = new ArrayList<>();
        Class<?> clazz = data.getClass();
        List<AttrsConfig> attrsConfigs = getAttrConfigsByModel(deviceInfo.getBrand());
        if (CollectionUtils.isNotEmpty(attrsConfigs)) {
            for (AttrsConfig conf : attrsConfigs) {
                try {
                    //反射获取枚举类 DoorControllerEnum中的拓展字段
                    Field f = ReflectionUtils.findField(clazz, conf.getAttrCode());
                    ReflectionUtils.makeAccessible(Objects.requireNonNull(f));
                    String value = (String) f.get(data);
                    ProjectDeviceAttrListVo attrVo = new ProjectDeviceAttrListVo();
                    attrVo.setAttrName(conf.getAttrName());
                    attrVo.setAttrCode(conf.getAttrCode());
                    attrVo.setAttrValue(value);
                    attrListVos.add(attrVo);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format("[%s::%s]取值失败", deviceInfo.getBrand(), conf.getAttrName()));
                }
            }
        }
        deviceInfo.setDeviceAttrs(attrListVos);
        return deviceInfo;
    }
}
