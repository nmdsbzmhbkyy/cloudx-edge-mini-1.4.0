package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.vo.DevicesResultVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>设备参数服务</p>
 *
 * @author : 王良俊
 * @date : 2021-10-19 17:01:58
 */
public interface DeviceParamConfigService extends BaseParamService{

    /**
     * <p>单台设备的参数设置已默认实现了（单台设备感觉都是通用的没必要单独实现）。</p>
     *
     * @param paramsNode 本次要设置的参数
     * @return 参数设置结果列表，通过这个确定每个参数设置成功或失败
     */
    List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId);

    /**
     * <p>批量设置设备参数的方法</p>
     *
     * @param paramsNode    批量设置/同步设置 包含多种参数的对象节点
     * @param deviceIdList  批量设置/同步设置 设备的ID列表
     * @param serviceIdList 批量设置/同步设置 参数的服务ID（这次要设置的参数的服务ID列表）
     * @return 批量设置结果对象
     */
    DevicesResultVo setMultiDeviceParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList);

}
