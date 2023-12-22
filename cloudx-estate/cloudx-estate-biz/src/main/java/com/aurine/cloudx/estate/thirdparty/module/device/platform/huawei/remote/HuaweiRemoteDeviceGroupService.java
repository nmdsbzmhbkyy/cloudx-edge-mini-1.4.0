package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.List;

/**
 * @ClassName: HuaweiRemoteDeviceGroupService
 * @author: 王良俊 <>
 * @date: 2020年11月24日 下午05:34:42
 * @Copyright:
 */
public interface HuaweiRemoteDeviceGroupService extends BaseRemote {

    /**
     * <p>
     * 添加设备组并返回设备组ID
     * </p>
     *
     * @param groupName 设备组名
     */
    HuaweiRespondDTO addDeviceGroup(HuaweiConfigDTO configDTO, String groupName);

    /**
     * <p>
     * 删除设备组并返回结果
     * </p>
     *
     * @param groupId 设备组ID
     */
    HuaweiRespondDTO delDeviceGroup(HuaweiConfigDTO configDTO, String groupId);

    /**
     * <p>
     * 查询设备组 返回设备组下设备列表等
     * </p>
     *
     * @param groupId 设备组ID
     * @param marker  用于记录分页标识,首次请求后返回
     */
    HuaweiRespondDTO queryDeviceGroup(HuaweiConfigDTO configDTO, String groupId, Integer pageNo, Integer pageSize, String marker);

    /**
     * <p>
     * 设备组设备管理
     * </p>
     *
     * @param groupId 设备组ID
     * @param deviceIdList 要添加到这个设备组的设备ID列表
     * @param active 向设备组添加或删除设备 addDevice/removeDevice
     */
    HuaweiRespondDTO deviceGroupManager(HuaweiConfigDTO configDTO, String groupId, List<String> deviceIdList, String active);


}
