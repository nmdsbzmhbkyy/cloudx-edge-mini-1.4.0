package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.DeviceParamDataVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表服务接口
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:15
 */
public interface ProjectDeviceParamInfoService extends IService<ProjectDeviceParamInfo> {

    /**
     * <p>
     * 根据设备ID获取到这台设备当前存放的参数数据（应该能获取到这台设备有的所有参数-设备目前有的参数的数据都会在里面存储）
     * </p>
     *
     * @param deviceId 设备ID
     */
    DeviceParamDataVo getParamByDeviceId(String deviceId, String productId);

    /**
     * <p>
     * 刷新设备参数
     * </p>
     *
     * @param deviceInfo
     * @author: 王良俊
     */
    boolean refreshParam(ProjectDeviceInfo deviceInfo);

    /**
     * <p>
     * 保存设备参数数据
     * </p>
     *
     * @param deviceParamInfoList 设备参数数据列表
     * @author: 王良俊
     */
    void saveDeviceParamBatch(List<ProjectDeviceParamInfo> deviceParamInfoList);

    /**
     * <p>
     *
     * </p>
     *
     * @param deviceId 设备ID
     * @return 状态json
     * {"doorState":0,"cloudCallState":0,"cloudPhoneState":0}
     * @author: 王良俊
     */
    String getDeviceOtherStatus(String deviceId);
}