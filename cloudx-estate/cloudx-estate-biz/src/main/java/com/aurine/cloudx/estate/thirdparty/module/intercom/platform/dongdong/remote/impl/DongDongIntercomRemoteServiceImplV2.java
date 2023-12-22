package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.config.DongdongConfig;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.core.DongdongDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.constant.DongDongErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.dto.DongDongRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote.DongDongIntercomRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 对接实现，用于拼接对接参数等逻辑
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Service
@Slf4j
public class DongDongIntercomRemoteServiceImplV2 implements DongDongIntercomRemoteService {
    @Resource
    DongdongDataConnector dongdongDataConnector;


    /**
     * 登录并返回userId
     *
     * @param username 登录用户名
     * @param password 登录密码
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO login(String username, String password) {

        String method = "loginWuyePlatform";
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", "");
        params.put("method", method);
        params.put("username", username);
        params.put("password", SecureUtil.md5(password).toLowerCase());

        return returnHandel(method, params);


    }

    /**
     * 修改登录用户信息
     *
     * @param email Email
     * @param phone 手机号
     */
    @Override
    public DongDongRespondDTO updateAccountInfo(String email, String phone) {

        String method = "editWuyeAccountInfo";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("email", email);
        params.put("mobilephone", phone);

        return returnHandel(method, params);
    }

    /**
     * 修改登录用户密码
     *
     * @param password 修改密码
     */
    @Override
    public DongDongRespondDTO updateAccountPassword(String password) {

        String method = "editWuyeAccountPassword";
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("password", SecureUtil.md5(password).toLowerCase());

        return returnHandel(method, params);
    }

    /**
     * 添加小区
     *
     * @param address     地址
     * @param villageName 小区名称   小区名称
     */
    @Override
    public DongDongRespondDTO addVillage(String address, String detailAddress, String villageName) {

        String method = "addVillage";
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("addr", address);
        params.put("detailaddr", detailAddress);
        params.put("villagename", villageName);

        return returnHandel(method, params);
    }

    /**
     * 删除小区
     *
     * @param villageId 小区ID
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO delVillage(Integer villageId) {

        String method = "delVillage";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));

        return returnHandel(method, params);
    }

    /**
     * 修改小区
     *
     * @param villageId     小区ID
     * @param address       地址
     * @param detailAddress 详细地址
     * @param villageName   小区名称
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO editVillage(Integer villageId, String address, String detailAddress, String villageName) {

        String method = "editVillage";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));
        params.put("addr", address);
        params.put("detailaddr", detailAddress);
        params.put("villagename", villageName);

        return returnHandel(method, params);
    }

    /**
     * 分页获取小区列表
     *
     * @param pageNo   页码 最大为10
     * @param pageSize 分页大小
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO pageVillages(Integer pageNo, Integer pageSize) {

        String method = "getVillages";
        Map<String, String> params = new HashMap<String, String>();

        String offset = String.valueOf((pageNo - 1) * pageSize);
        String size = String.valueOf(pageSize);

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("offset", offset);
        params.put("size", size);

        return returnHandel(method, params);
    }

    /**
     * 添加设备
     *
     * @param villageId  小区ID
     * @param sn         设备SN
     * @param deviceName 设备名称
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO addDevice(Integer villageId, String sn, String deviceName) {

        String method = "addDevice";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));
        params.put("sn", sn);
        params.put("devicename", deviceName);

        return returnHandel(method, params);
    }

    /**
     * 删除设备
     *
     * @param villageId 小区ID
     * @param deviceId  设备id
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO delDevice(Integer villageId, Integer deviceId) {

        String method = "delDevice";
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));
        params.put("deviceid", String.valueOf(deviceId));

        return returnHandel(method, params);
    }

    /**
     * 修改设备信息
     *
     * @param villageId  小区ID
     * @param deviceId   设备id
     * @param sn         设备SN      不支持修改
     * @param deviceName 设备名称
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO editDevice(Integer villageId, Integer deviceId, String sn, String deviceName) {

        String method = "editDevice";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));
        params.put("deviceid", String.valueOf(deviceId));
        params.put("sn", sn);
        params.put("devicename", deviceName);

        return returnHandel(method, params);
    }

    /**
     * 分页获取设备
     *
     * @param villageId 小区ID
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO pageDevices(Integer villageId, Integer pageNo, Integer pageSize) {

        String method = "getDevices";
        Map<String, String> params = new HashMap<String, String>();

        String offset = String.valueOf((pageNo - 1) * pageSize);
        String size = String.valueOf(pageSize);

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("villageid", String.valueOf(villageId));
        params.put("offset", offset);
        params.put("size", size);

        return returnHandel(method, params);
    }

    /**
     * 增加住户
     *
     * @param deviceId   设备id
     * @param roomCode   房间编码
     * @param memberName 住户名称
     * @param idNumber   住户身份证
     * @param phone      手机号
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO addMember(Integer deviceId, String roomCode, String memberName, String idNumber, String phone) {
        String method = "addMember";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("deviceid", String.valueOf(deviceId));
        params.put("roomnumber", roomCode);
        params.put("membername", memberName);
        if (StringUtils.isBlank(idNumber) || idNumber.contains("*")) {
            idNumber = null;
        }
        params.put("idnumber", idNumber);
        params.put("mobilephone", phone);

        return returnHandel(method, params);
    }

    /**
     * 删除住户
     *
     * @param deviceId 设备id
     * @param roomId   房间ID
     * @param memberId 住户ID
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO delMember(Integer deviceId, String roomId, String memberId) {

        String method = "delMember";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("deviceid", String.valueOf(deviceId));
        params.put("roomid", roomId);
        params.put("memberid", memberId);

        return returnHandel(method, params);
    }

    /**
     * 批量删除住户
     * {
     * [{
     * device,
     * [{roomId+memberId},{roomId+menberId}
     * ]
     * <p>
     * },
     * {
     * <p>
     * }]
     * }
     *
     * @param requestJson 请求参数
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO delMemberBatch(JSONArray requestJson) {

        String method = "batchDelMember";

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("drmids", requestJson.toJSONString());

        return returnHandel(method, params);
    }

    /**
     * 修改住户信息
     *
     * @param deviceId   设备id
     * @param roomId     房间ID
     * @param memberId   住户ID
     * @param roomCode   房间编码
     * @param memberName 住户名称
     * @param idNumber   住户身份证
     * @param phone      手机号
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO editMember(Integer deviceId, String roomId, String memberId, String roomCode, String memberName, String idNumber, String phone) {

        String method = "editMember";
        Map<String, String> params = new HashMap<String, String>();

        if (StringUtils.isBlank(idNumber) || idNumber.contains("*")) {
            idNumber = null;
        }

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("deviceid", String.valueOf(deviceId));
        params.put("roomid", roomId);
        params.put("memberid", memberId);
        params.put("roomnumber", roomCode);
        params.put("membername", memberName);
        params.put("idnumber", idNumber);
        params.put("mobilephone", phone);

        return returnHandel(method, params);
    }

    /**
     * 分页查询住户
     *
     * @param deviceId 设备id
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return 返回JSON
     */
    @Override
    public DongDongRespondDTO pageMembers(Integer deviceId, Integer pageNo, Integer pageSize) {

        String method = "getMembers";
        Map<String, String> params = new HashMap<String, String>();

        String offset = String.valueOf((pageNo - 1) * pageSize);
        String size = String.valueOf(pageSize);

        params.put("id", DongdongConfig.userId);
        params.put("method", method);
        params.put("deviceid", String.valueOf(deviceId));
        params.put("offset", offset);
        params.put("size", size);

        return returnHandel(method, params);
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V2.code;
    }

    /**
     * 返回数据处理
     * 判断返回信息是否可用，是否需要抛出异常
     *
     * @return
     */
    private DongDongRespondDTO returnHandel(String method, Map<String, String> paramMap) {
        JSONObject resultJson = dongdongDataConnector.post(method, paramMap);

        /**
         * 成功：
         * {"request_id":2867601,"response_params":{"memberid":{"roomid":"400453","memberid":"101452"}}}
         * {"request_id":9591130,"response_params":{"devices":[{"deviceid":"118085","devicename":"A区-1栋01单元梯口机-API","devicesn":"PXOQ926U0802986KRBIT","online":"1"},{"deviceid":"118076","devicename":"A区-2栋01单元立式梯口机-API","devicesn":"POOQ826U080298656DX6","online":"1"},{"deviceid":"118103","devicename":"区口机","devicesn":"PFOQ926U0802986UJ9C1","online":"1"}]}}
         *
         * 失败：
         * {"request_id":2827670,"error_code":13,"error_msg":"已经被添加"}
         * {"request_id":6247797,"error_code":11,"error_msg":"无效的小区ID"}
         */
        DongDongRespondDTO result = new DongDongRespondDTO();

        String requestId = resultJson.getString("request_id");
        String error_code = resultJson.getString("error_code");
        JSONObject response_params = null;
        JSONArray response_params_array = null;

        try {
            response_params = resultJson.getJSONObject("response_params");
        } catch (ClassCastException cce) {
            response_params_array = resultJson.getJSONArray("response_params");
        }

        if (StringUtils.isEmpty(error_code)) {
            result.setErrorEnum(DongDongErrorEnum.SUCCESS);
        } else {
            result.setErrorEnum(DongDongErrorEnum.getByCode(error_code));
            log.error("[咚咚云对讲] 发生异常：{}", DongDongErrorEnum.getByCode(error_code).value);
        }

        result.setRequestId(requestId);
        result.setRespondObj(response_params);
        result.setRespondArray(response_params_array);

        return result;
    }


}
