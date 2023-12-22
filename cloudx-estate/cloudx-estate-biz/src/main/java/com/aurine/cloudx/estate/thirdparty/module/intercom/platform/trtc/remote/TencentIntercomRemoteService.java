package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto.TencentRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto.TencentUserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: cloudx
 * @description: 对接实现，用于拼接对接参数等逻辑
 * @author: 谢泽毅
 * @create: 2021-08-11 14:29
 **/
public interface TencentIntercomRemoteService {
    /**
     * 获取版本
     *
     * @return
     */
    String getVersion();

    /**
     * 【项目】 创建项目 POST
     *
     * @param enabled     0禁用 1启用
     * @param projectId   NOT NULL
     * @param projectName
     * @param rtcCode     TRTC
     * @return
     */
    TencentRespondDTO saveProject(Integer enabled, String projectId, String projectName, String rtcCode);

    /**
     * 【项目】 更新项目 PUT
     *
     * @param enabled     0禁用 1启用
     * @param projectId   NOT NULL
     * @param projectName
     * @param rtcCode     TRTC
     * @return
     */
    TencentRespondDTO updateProject(Integer enabled, String projectId, String projectName, String rtcCode);

    /**
     * 【房屋】 创建房屋 POST
     *
     * @param enabled   0禁用 1启用
     * @param projectId NOT NULL
     * @param roomId    NOT NULL
     * @param roomName
     * @return
     */
    TencentRespondDTO saveRoom(Integer enabled, String projectId, String roomId, String roomName);

    /**
     * 【房屋】 修改房屋 PUT
     *
     * @param enabled   0禁用 1启用
     * @param projectId NOT NULL
     * @param roomId    NOT NULL
     * @param roomName
     * @return
     */
    TencentRespondDTO updateRoom(Integer enabled, String projectId, String roomId, String roomName);

    /**
     * 【房屋】 启用/禁用房屋的RTC(修改房屋RTC能力) PUT
     *
     * @param enabled   NOT NULL 0禁用 1启用
     * @param projectId NOT NULL
     * @param roomId    NOT NULL 等价于Put里的roomNo
     * @return
     */
    TencentRespondDTO updateRoomEnabled(Integer enabled, String projectId, String roomId);

    /**
     * 【用户-房屋】 将多个用户添加到房屋中 POST
     *
     * @param projectId
     * @param registerIdList NOT NULL
     * @param roomId         等价于Put里的roomNo
     * @param successUsers
     * @return
     */
    TencentRespondDTO saveUserListToRoom(String projectId, List<String> registerIdList, String roomId, List<String> successUsers);

    /**
     * 【用户-房屋】 删除房屋中的多个用户 DELETE
     *
     * @param projectId
     * @param registerIdList NOT NULL
     * @param roomId         等价于Put里的roomNo
     * @param successUsers
     * @return
     */
    TencentRespondDTO removeUserListToRoom(String projectId, List<String> registerIdList, String roomId, List<String> successUsers);

    /**
     * 【用户-房屋】 获取房屋下的用户列表 GET
     *
     * @param projectId NOT NULL
     * @param roomId    NOT NULL
     * @return
     */
    TencentRespondDTO getRoomUserList(String projectId, String roomId);

    /**
     * 【用户-设备】 将多个用户添加(绑定)到设备中
     *
     * @param deviceId       NOT NULL
     * @param projectId      NOT NULL
     * @param registerIdList
     * @param successUsers
     * @return
     */
    TencentRespondDTO saveUserListToDevice(String deviceId, String projectId, List<String> registerIdList, List<String> successUsers);

    /**
     * 【用户-设备】 删除（解除）设备下绑定的多个用户 POST
     *
     * @param deviceId       NOT NULL
     * @param projectId      NOT NULL
     * @param registerIdList
     * @param successUsers
     * @return
     */
    TencentRespondDTO removeUserListFromDevice(String deviceId, String projectId, List<String> registerIdList, List<String> successUsers);

    /**
     * 【用户-设备】 将 设备 用户 房屋 进行关联 POST
     *
     * @param deviceId       NOT NULL
     * @param iotUserId      NOT NULL
     * @param productId      NOT NULL
     * @param roomId         NOT NULL
     * @param rtcType        NOT NULL
     * @param userRegisterId NOT NULL
     * @param userType
     * @return
     */
    TencentRespondDTO saveUserDeviceRoomRel(String deviceId, String iotUserId, String productId, String roomId, String rtcType,
                                            String userRegisterId, Integer userType);

    /**
     * 【用户-设备】 解除 设备 用户 房屋 的关联 POST
     *
     * @param deviceId       NOT NULL
     * @param iotUserId      NOT NULL
     * @param productId      NOT NULL
     * @param roomId         NOT NULL
     * @param rtcType        NOT NULL
     * @param userRegisterId NOT NULL
     * @param userType
     * @return
     */
    TencentRespondDTO removeUserDeviceRoomRel(String deviceId, String iotUserId, String productId, String roomId, String rtcType,
                                              String userRegisterId, Integer userType);

    /**
     * 【用户】 获取iot用户信息 GET
     *
     * @param registerId NOT NULL
     * @param rtcType NOT NULL
     * @return
     */
    TencentRespondDTO getUserIotInfo(String registerId, String rtcType);


    /**
     * 【用户】 更新(修改)用户信息 PUT
     *
     * @param appId      NOT NULL 应用id
     * @param enabled    NOT NULL 0禁用 1启用
     * @param projectId  NOT NULL
     * @param registerId NOT NULL
     * @param userType   NOT NULL
     * @param createTime
     * @param iotUserId
     * @param userImage
     * @param UserName
     * @param UserNick
     * @return
     */
    TencentRespondDTO updateUser(String appId, Integer enabled, String projectId, String registerId, Integer userType,
                                 String createTime, String iotUserId, String userImage, String UserName, String UserNick);

    /**
     * 【用户】 增加（导入）用户 POST
     *
     * @param rtcType
     * @param appId      NOT NULL 应用id
     * @param enabled    NOT NULL 0禁用 1启用
     * @param projectId  NOT NULL
     * @param registerId NOT NULL
     * @param userType   NOT NULL
     * @param createTime
     * @param iotUserId
     * @param userImage
     * @param UserName
     * @param UserNick
     * @return
     */
    TencentRespondDTO saveUser(String rtcType, String appId, Integer enabled, String projectId, Integer registerId, String telephone, Integer userType,
                               String createTime, String iotUserId, String userImage, String UserName, String UserNick);

    /**
     * 【用户】 增加单个（导入）用户与多个房屋及多个设备的关系 POST
     *
     * @param deviceIds
     * @param roomIds
     * @param rtcType
     * @param appId
     * @param enabled
     * @param projectId
     * @param registerId
     * @param userType
     * @param createTime
     * @param iotUserId
     * @param userImage
     * @param UserName
     * @param UserNick
     * @return
     */
    TencentRespondDTO saveUserAndDevicesRoomsRel(List<String> deviceIds, List<String> roomIds, String rtcType, String appId, Integer enabled,
                                                 String projectId, String registerId, Integer userType, String createTime, String iotUserId, String userImage, String UserName, String UserNick);

    /**
     * 【用户】 增加多个（导入）用户与多个房屋及多个设备的关系 POST
     *
     * @return
     */
    TencentRespondDTO saveUsersAndDevicesRoomsRel(Map<String, Map<String, Set<String>>> telephoneMap, Integer projectId, Integer enabled, String rtcType);


    /**
     * 【用户】 获取用户iot令牌 GET
     *
     * @param registerId NOT NULL
     * @param rtcType    NOT NULL
     * @return
     */
    TencentRespondDTO getUserIotToken(String registerId, String rtcType);

    /**
     * 【设备】 修改（更新）用户设备信息 PUT
     *
     * @param deviceId     NOT NULL
     * @param projectId    NOT NULL
     * @param deviceName
     * @param deviceSecret
     * @param deviceSn
     * @param enabled
     * @param productId
     * @return
     */
    TencentRespondDTO updateDevice(String deviceId, String projectId, String deviceName, String deviceSecret, String deviceSn,
                                   Integer enabled, String productId);

    /**
     * 【设备】 设备行为同步下发 POST
     *
     * @param actionId    NOT NULL
     * @param deviceId    NOT NULL
     * @param inputParams NOT NULL
     * @param productId   NOT NULL
     * @param rtcType
     * @return
     */
    TencentRespondDTO getUserIotToken(String actionId, String deviceId, Object inputParams, String productId, String rtcType);

    /**
     * 【设备】 将设备绑定到小区 (3.0中台使用) (修改小区与设备的信息) PUT
     *
     * @param deviceId     NOT NULL
     * @param projectId    NOT NULL
     * @param deviceName
     * @param deviceSecret
     * @param deviceSn
     * @param enabled
     * @param productId
     * @return
     */
    TencentRespondDTO updateDeviceProject(String deviceId, String projectId, String deviceName, String deviceSecret, String deviceSn,
                                          String enabled, String productId);

    /**
     * 【设备】 创建设备的同时绑定到小区 POST
     *
     * @param deviceId     NOT NULL
     * @param deviceName
     * @param deviceSecret
     * @param deviceSn
     * @param enabled
     * @param productId    NOT NULL
     * @return
     */
    TencentRespondDTO saveDeviceProject(String deviceId, String deviceName, String deviceSecret, String deviceSn,
                                        Integer enabled, String productId);

    /**
     * 【设备】 获取设备登录信息 POST
     *
     * @param deviceSn    NOT NULL
     * @param productId   NOT NULL
     * @param accesstoken
     * @return
     */
    TencentRespondDTO getDeviceLogInfo(String deviceSn, String productId, Object accesstoken);

    /**
     * 【用户】 对已开启云服务但未注册的用户进行APP上的注册
     *
     * @param register
     * @param rtcType
     * @return
     */
    TencentRespondDTO saveAppUserRegister(String register, String rtcType);

}
