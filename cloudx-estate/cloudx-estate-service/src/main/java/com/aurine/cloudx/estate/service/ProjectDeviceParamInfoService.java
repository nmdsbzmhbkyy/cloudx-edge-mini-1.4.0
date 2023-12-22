package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.swing.*;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表服务接口
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:15
 */
public interface ProjectDeviceParamInfoService extends IService<ProjectDeviceParamInfo> {

    /**
     * <p>
     *  根据设备ID获取到这台设备当前存放的参数数据（应该能获取到这台设备有的所有参数-设备目前有的参数的数据都会在里面存储）
     * </p>
     *
     * @param deviceId 设备ID
     */
    DeviceParamDataVo getParamByDeviceId(String deviceId, String productId);
}