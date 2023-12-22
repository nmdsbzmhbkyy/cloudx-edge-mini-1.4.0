

package com.aurine.cloudx.estate.thirdparty.module.wr20.callback;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.BaseService;

/**
 * WR20 住户接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20PersonCallbackService extends BaseService {


    /**
     * 新增住户
     *
     * @param jsonObject
     * @return
     */
    void addPerson(JSONObject jsonObject, String gatewayId, String uid);

    /**
     * 新增一人多房住户
     *
     * @param jsonObject
     * @return
     */
    void checkInPerson(JSONObject jsonObject, String gatewayId, String uid);

    /**
     * 删除一人多房住户
     *
     * @param jsonObject
     * @return
     */
    void checkOutPerson(JSONObject jsonObject, String gatewayId, String uid);

    /**
     * 新增员工
     *
     * @param jsonObject
     * @return
     */
    void addStaff(JSONObject jsonObject, String gatewayId, String uid);

    /**
     * 添加访客
     *
     * @param jsonObject
     * @param gatewayId
     * @param uid
     */
    void addVisitor(JSONObject jsonObject, String gatewayId, String uid);


    /**
     * WR20侧 新增住户 回调
     *
     * @param jsonObject
     * @return
     */
    void addPersonByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * WR20侧 更新住户 回调
     *
     * @param jsonObject
     * @return
     */
    void updatePersonByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * WR20侧 删除住户 回调
     *
     * @param jsonObject
     * @return
     */
    void delPersonByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * WR20侧 新增员工 回调
     *
     * @param jsonObject
     * @return
     */
    void addStaffByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * WR20侧 更新员工 回调
     *
     * @param jsonObject
     * @return
     */
    void updateStaffByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * WR20侧 删除员工 回调
     *
     * @param jsonObject
     * @return
     */
    void delStaffByWR20(JSONObject jsonObject, String gatewayId);

    /**
     * 同步住户
     *
     * @param jsonObject
     * @param gatewayId
     */
    void syncTenement(JSONObject jsonObject, String gatewayId);

    /**
     * 同步员工
     *
     * @param jsonObject
     * @param gatewayId
     */
    void syncStaff(JSONObject jsonObject, String gatewayId);

}
