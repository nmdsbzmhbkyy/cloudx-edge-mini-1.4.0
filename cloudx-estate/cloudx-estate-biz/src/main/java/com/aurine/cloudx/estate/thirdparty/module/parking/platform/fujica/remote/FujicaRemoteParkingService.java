

package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 赛菲姆停车场接入服务
 */
public interface FujicaRemoteParkingService extends BaseRemote {

    /**
     * 获取车场信息
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    JSONObject getParkInfo(String gid, String appid, Integer projectId, Map<String, Object> paramMap);

    /**
     * 获取车道摄像头设备列表
     *
     * @param gid 车场code
     * @return
     */
    JSONArray getLaneCameraList(String gid, String appid, Integer projectId, Map<String, Object> paramMap);


    /**
     * 添加车辆
     *
     * @param gid            车场ID
     * @param appid
     * @param projectId
     * @param authDeviceList 可通行设备ID列表
     * @param plate          车牌号
     * @param staffNo        人员编号 SI + personSeq
     * @param staffName      人员名称
     * @param telphoneNo     电话号码
     * @param orginazitionId 人员所属组织ID
     * @param tcm            卡类型ID（收费类型）
     * @param beginTime      开始事件
     * @param endTime        结束时间
     * @param money          首次充值，没有充值写入null
     * @param parkingPlaceNo 车位编号
     * @param paramMap
     * @return
     */
    boolean addCar(String gid, String appid, Integer projectId, List<String> authDeviceList, String plate, String staffNo, String staffName, String telphoneNo, String orginazitionId, String tcm, LocalDateTime beginTime, LocalDateTime endTime, Double money, String parkingPlaceNo, Map<String, Object> paramMap);


    /**
     * 延期与充值
     *
     * @param gid       车场ID
     * @param appid
     * @param projectId
     * @param money         充值金额
     * @param beginTime     开始时间
     * @param endTime       结束时间
     * @param plate         车牌号
     * @param paramMap
     * @return
     */
    boolean extraDate(String gid, String appid, Integer projectId, Double money, LocalDateTime beginTime, LocalDateTime endTime, String plate, Map<String, Object> paramMap);

    /**
     * 注销车辆
     *
     * @param gid  车场ID
     * @param plate    车牌号
     * @param appid
     * @param paramMap 扩展预留参数
     * @return
     */
    boolean removeMthCar(String gid, String appid, Integer projectId, String plate, Map<String, Object> paramMap);

    /**
     * 获取默认组织
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    JSONObject getDefaultOrg(String gid, String appid, Integer projectId, Map<String, Object> paramMap);


    /**
     * 获取支付类型
     * （卡类型）
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    JSONObject getPayType(String gid, String appid, Integer projectId, Map<String, Object> paramMap);

}
