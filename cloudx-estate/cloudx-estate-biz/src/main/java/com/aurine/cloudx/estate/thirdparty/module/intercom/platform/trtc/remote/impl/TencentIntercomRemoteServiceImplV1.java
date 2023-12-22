package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.config.TencentConfig;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.core.TencentDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.constant.TencentErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto.TencentRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.TencentIntercomRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: cloudx
 * @description:
 * @author: 谢泽毅
 * @create: 2021-08-11 14:29
 **/
@Service
@Slf4j
public class TencentIntercomRemoteServiceImplV1 implements TencentIntercomRemoteService {

    @Resource
    private TencentDataConnector tencentDataConnector;

    private String baseUrl = TencentConfig.baseUrl;


    @Override
    public TencentRespondDTO saveProject(Integer enabled, String projectId, String projectName, String rtcCode) {
        String uri = "project";

        JSONObject requestJson = new JSONObject();
        requestJson.put("enabled", enabled);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("projectName", projectName);
        requestJson.put("rtcCode", rtcCode);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson,null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);
        log.info("[腾讯云对讲] 项目创建：{}", respJson);
        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO updateProject(Integer enabled, String projectId, String projectName, String rtcCode) {
        String uri = "project";

        JSONObject requestJson = new JSONObject();
        requestJson.put("enabled", enabled);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("projectName", projectName);
        requestJson.put("rtcCode", rtcCode);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.PUT);

        if (enabled == 1) {
            log.info("[腾讯云对讲] 项目启动：{}", respJson);
        } else {
            log.info("[腾讯云对讲] 项目关闭：{}", respJson);
        }

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO saveRoom(Integer enabled, String projectId, String roomId, String roomName) {
        String uri = "room";

        JSONObject requestJson = new JSONObject();
        requestJson.put("enabled", enabled);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("roomId", roomId);
        requestJson.put("roomName", roomName);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 房屋创建：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO updateRoom(Integer enabled, String projectId, String roomId, String roomName) {
        String uri = "room";

        JSONObject requestJson = new JSONObject();
        requestJson.put("enabled", enabled);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("roomId", roomId);
        requestJson.put("roomName", roomName);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.PUT);

        if (enabled == 1) {
            log.info("[腾讯云对讲] 房屋启用云对讲：{}", respJson);
        } else {
            log.info("[腾讯云对讲] 房屋禁用云对讲：{}", respJson);
        }

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO updateRoomEnabled(Integer enabled, String projectId, String roomId) {
        return null;
    }

    @Override
    public TencentRespondDTO saveUserListToRoom(String projectId, List<String> registerIdList, String roomId, List<String> successUsers) {
        return null;
    }

    @Override
    public TencentRespondDTO removeUserListToRoom(String projectId, List<String> registerIdList, String roomId, List<String> successUsers) {
        String uri = "user/room";

        JSONObject requestJson = new JSONObject();
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("registerIdList", registerIdList);
        requestJson.put("roomNo", roomId);
        requestJson.put("successUsers", successUsers);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.DELETE);

        log.info("[腾讯云对讲] 房屋与住户解绑：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO getRoomUserList(String projectId, String roomId) {
        return null;
    }

    @Override
    public TencentRespondDTO saveUserListToDevice(String deviceId, String projectId, List<String> registerIdList, List<String> successUsers) {
        String uri = "user/device/batchBind";

        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceId", deviceId);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("registerIdList", registerIdList);
        requestJson.put("successUsers", successUsers);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 设备与住户绑定：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO removeUserListFromDevice(String deviceId, String projectId, List<String> registerIdList, List<String> successUsers) {
        String uri = "user/device/batchUnBind";

        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceId", deviceId);
        requestJson.put("projectId", "S" + projectId);
        requestJson.put("registerIdList", registerIdList);
        requestJson.put("successUsers", successUsers);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 设备与住户解绑：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO saveUserDeviceRoomRel(String deviceId, String iotUserId, String projectId, String roomId, String rtcType, String userRegisterId, Integer userType) {
//        String uri = "user/device/bind";
//
//        JSONObject requestJson = new JSONObject();
//        requestJson.put("deviceId",deviceId);
//        requestJson.put("iotUserId", iotUserId);
//        requestJson.put("projectId", "S"+projectId);
//        requestJson.put("roomId", roomId);
//        requestJson.put("rtcType",rtcType);
//        requestJson.put("userRegisterId", userRegisterId);
//        requestJson.put("userType", userType);
//
//        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson,null);
//        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);
//        log.info("[腾讯云对讲] 新增绑定(用户 设备 房屋)：{}", respJson);

//        return returnHandel(respJson);
        return null;
    }

    @Override
    public TencentRespondDTO removeUserDeviceRoomRel(String deviceId, String iotUserId, String productId, String roomId, String rtcType, String userRegisterId, Integer userType) {
        String uri = "user/device/unbind";

        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceId", deviceId);
        requestJson.put("iotUserId", iotUserId);
        requestJson.put("productId", "S" + productId);
        requestJson.put("roomId", roomId);
        requestJson.put("rtcType", rtcType);
        requestJson.put("userRegisterId", userRegisterId);
        requestJson.put("userType", userType);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 解除绑定(用户 设备 房屋)：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO getUserIotInfo(String registerId, String rtcType) {
        return null;
    }

    @Override
    public TencentRespondDTO updateUser(String appId, Integer enabled, String projectId, String registerId, Integer userType, String createTime, String iotUserId, String userImage, String UserName, String UserNick) {
        return null;
    }

    @Override
    public TencentRespondDTO saveUser(String rtcType, String appId, Integer enabled, String projectId, Integer registerId, String telephone, Integer userType, String createTime, String iotUserId, String userImage, String UserName, String UserNick) {
        String uri = "user/import";

        JSONObject requestJson = new JSONObject();
        requestJson.put("rtcType", rtcType);
        // 子数组对象
        JSONArray userArr = new JSONArray();
        JSONObject user = new JSONObject();
        user.put("appId", appId);
        user.put("enabled", enabled);
        user.put("projectId", "S" + projectId);
        user.put("registerId", registerId);
        user.put("telephone", telephone);
        user.put("userType", userType);
        user.put("createTime", createTime);
        user.put("iotUserId", iotUserId);
        user.put("userImage", userImage);
        user.put("userName", UserName);
        user.put("userNick", UserNick);
        userArr.add(user);
        requestJson.put("user", userArr);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 新增住户：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO saveUserAndDevicesRoomsRel(List<String> deviceIds, List<String> roomIds, String rtcType, String appId, Integer enabled,
                                                        String projectId, String registerId, Integer userType, String createTime, String iotUserId, String userImage, String UserName, String UserNick) {
        String uri = "user/importUserAndRel";

        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceIds", deviceIds);
        requestJson.put("roomIds", roomIds);
        requestJson.put("rtcType", rtcType);

        // 子数组对象
        JSONObject user = new JSONObject();
        user.put("appId", appId);
        user.put("enabled", enabled);
        user.put("projectId", "S" + projectId);
        user.put("registerId", registerId);
        user.put("userType", userType);
        user.put("createTime", createTime);
        user.put("iotUserId", iotUserId);
        user.put("userImage", userImage);
        user.put("userName", UserName);
        user.put("userNick", UserNick);
        requestJson.put("user", user);
        JSONArray requestArrJson = new JSONArray();
        requestArrJson.add(requestJson);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(null, requestArrJson);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 单住户与多个设备和多个房屋关联：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO saveUsersAndDevicesRoomsRel(Map<String, Map<String, Set<String>>> UsersDevicesRoomsMap, Integer projectId, Integer enabled, String rtcType) {
        String uri = "user/importUserAndRel";

        JSONArray requestArrJson = new JSONArray();
        for (String telephone : UsersDevicesRoomsMap.keySet()) {
            List<String> userTypes = gettelephoneMapSetToList(UsersDevicesRoomsMap, telephone, "userType");
            if ("null".equals(userTypes.get(0))) {
                continue;
            }
            JSONObject requestJson = new JSONObject();
            List<String> deviceIds = gettelephoneMapSetToList(UsersDevicesRoomsMap, telephone, "deviceIds");
            List<String> roomIds = gettelephoneMapSetToList(UsersDevicesRoomsMap, telephone, "roomIds");
            requestJson.put("deviceIds", deviceIds);
            requestJson.put("roomIds", roomIds);
            requestJson.put("rtcType", rtcType);
            // 子数组对象
            JSONObject user = new JSONObject();
            user.put("appId", projectId.toString());
            user.put("enabled", enabled);
            user.put("projectId", "S" + projectId.toString());
            user.put("registerId", telephone);
            user.put("userType", Integer.valueOf(userTypes.get(0)));
            requestJson.put("user", user);
            requestArrJson.add(requestJson);
        }

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(null, requestArrJson);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);

        log.info("[腾讯云对讲] 批量关联：批量关联房屋-住户，设备-住户：{}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO getUserIotToken(String registerId, String rtcType) {
        return null;
    }

    @Override
    public TencentRespondDTO updateDevice(String deviceId, String projectId, String deviceName, String deviceSecret, String deviceSn, Integer enabled, String productId) {
        String uri = "device/manage";

        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceId", deviceId);
        requestJson.put("deviceName", deviceName);
        requestJson.put("deviceSecret", deviceSecret);
        requestJson.put("deviceSn", deviceSn);
        requestJson.put("enabled", enabled);
        requestJson.put("projectId", "S" + projectId);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.PUT);

        if (enabled == 1) {
            log.info("[腾讯云对讲] 启用设备：{}", respJson);
        } else {
            log.info("[腾讯云对讲] 禁用设备：{}", respJson);
        }

        return returnHandel(respJson);
    }

    @Override
    public TencentRespondDTO getUserIotToken(String actionId, String deviceId, Object inputParams, String productId, String rtcType) {
        return null;
    }

    @Override
    public TencentRespondDTO updateDeviceProject(String deviceId, String projectId, String deviceName, String deviceSecret, String deviceSn, String enabled, String productId) {
//        String uri = "device/manage/bindProjectId";
//
//        JSONObject requestJson = new JSONObject();
//        requestJson.put("deviceId",deviceId);
//        requestJson.put("deviceName", deviceName);
//        requestJson.put("deviceSecret", deviceSecret);
//        requestJson.put("deviceSn", deviceSn);
//        requestJson.put("enabled",enabled);
//        requestJson.put("productId", productId);
//
//        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson,null);
//        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.PUT);
//        log.info("[腾讯云对讲] 绑定设备小区：{}", respJson);

//        JSONObject respJson = aliEdgeDataConnector.put(baseUrl+uri, requestJson);
//        log.info("[阿里边缘] 腾讯云对讲绑定设备小区：{}", respJson);
//        return returnHandel(respJson);
        return null;
    }

    @Override
    public TencentRespondDTO saveDeviceProject(String deviceId, String deviceName, String deviceSecret, String deviceSn, Integer enabled, String projectId) {
//        String uri = "device/manage/create";
//
//        JSONObject requestJson = new JSONObject();
//        requestJson.put("deviceId",deviceId);
//        requestJson.put("deviceName", deviceName);
//        requestJson.put("deviceSecret", deviceSecret);
//        requestJson.put("deviceSn", deviceSn);
//        requestJson.put("enabled",enabled);
//        requestJson.put("projectId", projectId);
//
//        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson,null);
//        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.POST);
//        log.info("[腾讯云对讲] 创建设备：{}", respJson);

//        JSONObject respJson = aliEdgeDataConnector.post(baseUrl+uri, requestJson);
//        log.info("[阿里边缘] 腾讯云对讲创建设备：{}", respJson);
//        return returnHandel(respJson);
        return null;
    }

    @Override
    public TencentRespondDTO getDeviceLogInfo(String deviceSn, String productId, Object accesstoken) {
        return null;
    }

    @Override
    public TencentRespondDTO saveAppUserRegister(String registerId, String rtcType) {
        String uri = "user";

        JSONObject requestJson = new JSONObject();
        requestJson.put("registerId", registerId);
        requestJson.put("rtcType", rtcType);

        TencentRespondDTO tencentRespondDTO = new TencentRespondDTO(requestJson, null);
        JSONObject respJson = tencentDataConnector.conn(baseUrl + uri, tencentRespondDTO, HttpMethod.GET);

        log.info("[腾讯云对讲] APP注册新住户 {}", respJson);

        return returnHandel(respJson);
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }



    /**
     * 返回数据处理
     * 判断返回信息是否可用，是否需要抛出异常
     *
     * @return
     */
    private TencentRespondDTO returnHandel(JSONObject respondJson) {
        TencentRespondDTO result = new TencentRespondDTO();
        // 腾讯云对讲 远程调用返回对象为空
        if (respondJson == null) {
            result.setCode(TencentErrorEnum.OTHER_CODE.code);
            result.setMsg(TencentErrorEnum.OTHER_CODE.value);
            return result;
        }

        result.setCode(respondJson.getString("code"));
        result.setMsg(respondJson.getString("msg"));
        try {
            JSONObject respondObj = respondJson.getJSONObject("data");
            result.setRespondObj(respondObj);
        } catch (Exception e) {
            JSONArray respondArray = respondJson.getJSONArray("data");
            result.setRespondArray(respondArray);
        }

        return result;
    }
    /**
     * 将telephoneMap下的set转换成list
     *
     * @param telephoneMap
     * @param telephone
     * @param type
     * @return
     */
    private List<String> gettelephoneMapSetToList(Map<String, Map<String, Set<String>>> telephoneMap, String telephone, String type) {
        List<String> result = new ArrayList<>();
        for (String deviceId : telephoneMap.get(telephone).get(type)) {
            result.add(deviceId);
        }
        return result;
    }
}
