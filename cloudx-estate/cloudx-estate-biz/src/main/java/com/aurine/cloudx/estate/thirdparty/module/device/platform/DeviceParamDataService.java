package com.aurine.cloudx.estate.thirdparty.module.device.platform;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.enums.DeviceManufactureEnum;
import com.aurine.cloudx.estate.vo.DevicesResultVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Set;

/**
 * <p>设备参数服务</p>
 *
 * @author : 王良俊
 * @date : 2021-10-19 17:01:58
 */
public interface DeviceParamDataService extends BaseParamService {

    /**
     * <p>设备属性相关数据的处理（就是设备参数怎么保存）</p>
     *
     * @param json       设备回调的json数据
     * @param deviceInfo 属性所属设备的设备信息对象
     */
    void deviceDataUpdate(String json, ProjectDeviceInfo deviceInfo);

    /**
     * <p>请求上报设备参数</p>
     *
     * @param serviceIdSet 服务ID集合
     * @param deviceId     设备ID
     */
    void requestDeviceParam(Set<String> serviceIdSet, String deviceId);

}
