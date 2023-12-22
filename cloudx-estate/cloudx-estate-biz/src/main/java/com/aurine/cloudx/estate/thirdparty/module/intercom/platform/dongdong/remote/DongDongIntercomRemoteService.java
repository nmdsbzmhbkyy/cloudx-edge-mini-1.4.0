

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.remote;

import com.alibaba.fastjson.JSONArray;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.dto.DongDongRespondDTO;

/**
 * <p> 咚咚 云对讲 remote 接口</p>
 *
 * @ClassName: DongDongRemoteIntegerercomService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-13 17:21
 * @Copyright:
 */
public interface DongDongIntercomRemoteService extends BaseRemote {

    /**
     * 登录并返回userId
     *
     * @param username 登录用户名
     * @param password 登录密码
     * @return 返回JSON
     */
    DongDongRespondDTO login(String username, String password);


    /**
     * 修改登录用户信息
     *
     * @param email  Email
     * @param phone  手机号
     */
    DongDongRespondDTO updateAccountInfo(String email, String phone);

    /**
     * 修改登录用户密码
     *
     * @param password 修改密码
     */
    DongDongRespondDTO updateAccountPassword(String password);

    /**
     * 添加小区
     *
     * @param address     地址
     * @param villageName 小区名称   小区名称
     */
    DongDongRespondDTO addVillage(String address, String detailAddress, String villageName);

    /**
     * 删除小区
     *
     * @param villageId 小区ID
     * @return 返回JSON
     */
    DongDongRespondDTO delVillage(Integer villageId);


    /**
     * 修改小区
     *
     * @param villageId     小区ID
     * @param address       地址
     * @param detailAddress 详细地址
     * @param villageName   小区名称
     * @return 返回JSON
     */
    DongDongRespondDTO editVillage(Integer villageId, String address, String detailAddress, String villageName);


    /**
     * 分页获取小区列表
     *
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return 返回JSON
     */
    DongDongRespondDTO pageVillages(Integer pageNo, Integer pageSize);


    /**
     * 添加设备
     *
     * @param villageId  小区ID
     * @param sn         设备SN
     * @param deviceName 设备名称
     * @return 返回JSON
     */
    DongDongRespondDTO addDevice(Integer villageId, String sn, String deviceName);


    /**
     * 删除设备
     *
     * @param villageId 小区ID
     * @param deviceId  设备id
     * @return 返回JSON
     */
    DongDongRespondDTO delDevice(Integer villageId, Integer deviceId);


    /**
     * 修改设备信息
     *
     * @param villageId  小区ID
     * @param deviceId   设备id
     * @param sn         设备SN      不支持修改
     * @param deviceName 设备名称
     * @return 返回JSON
     */
    DongDongRespondDTO editDevice(Integer villageId, Integer deviceId, String sn, String deviceName);


    /**
     * 分页获取设备
     *
     * @param villageId 小区ID
     * @param pageNo    页码
     * @param pageSize  分页大小
     * @return 返回JSON
     */
    DongDongRespondDTO pageDevices(Integer villageId, Integer pageNo, Integer pageSize);

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
    DongDongRespondDTO addMember(Integer deviceId, String roomCode, String memberName, String idNumber, String phone);


    /**
     * 删除住户
     *
     * @param deviceId 设备id
     * @param roomId   房间ID
     * @param memberId 住户ID
     * @return 返回JSON
     */
    DongDongRespondDTO delMember(Integer deviceId, String roomId, String memberId);


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
    DongDongRespondDTO delMemberBatch(JSONArray requestJson);

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
    DongDongRespondDTO editMember(Integer deviceId, String roomId, String memberId, String roomCode, String memberName, String idNumber, String phone);

    /**
     * 分页查询住户
     *
     * @param deviceId 设备id
     * @param pageNo   页码
     * @param pageSize 分页大小
     * @return 返回JSON
     */
    DongDongRespondDTO pageMembers(Integer deviceId, Integer pageNo, Integer pageSize);


}
