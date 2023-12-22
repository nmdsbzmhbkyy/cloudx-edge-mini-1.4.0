package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.List;

/**
 * @ClassName: HuaweiRemoteDeviceGroupService
 * @author: 王良俊 <>
 * @date: 2020年11月24日 下午05:34:42
 * @Copyright:
 */
public interface AurineEdgeRemoteDeviceGroupService extends BaseRemote {

    /**
     * <p>
     * 添加设备组并返回设备组ID
     * </p>
     *
     * @param groupName 设备组名
     */
    AurineEdgeRespondDTO addDeviceGroup(AurineEdgeConfigDTO configDTO, String groupName);

    /**
     * <p>
     * 删除设备组并返回结果
     * </p>
     *
     * @param groupId 设备组ID
     */
    AurineEdgeRespondDTO delDeviceGroup(AurineEdgeConfigDTO configDTO, String groupId);

    /**
     * <p>
     * 查询设备组 返回设备组下设备列表等
     * </p>
     *
     * @param groupId 设备组ID
     * @param marker  用于记录分页标识,首次请求后返回
     */
    AurineEdgeRespondDTO queryDeviceGroup(AurineEdgeConfigDTO configDTO, String groupId, Integer pageNo, Integer pageSize, String marker);

    /**
     * <p>
     * 设备组设备管理
     * </p>
     *
     * @param groupId      设备组ID
     * @param deviceIdList 要添加到这个设备组的设备ID列表
     * @param active       向设备组添加或删除设备 addDevice/removeDevice
     */
    AurineEdgeRespondDTO deviceGroupManager(AurineEdgeConfigDTO configDTO, String groupId, List<String> deviceIdList, String active);


}
