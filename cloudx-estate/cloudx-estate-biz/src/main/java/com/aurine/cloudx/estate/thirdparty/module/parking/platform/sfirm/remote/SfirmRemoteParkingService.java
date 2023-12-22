

package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.Map;

/**
 * 赛菲姆停车场接入服务
 */
public interface SfirmRemoteParkingService extends BaseRemote {


    /**
     * 添加车主
     *
     * @param parkKey     车场ID
     * @param userName    车主名
     * @param carTypeNo   车辆类型编码	收费规则
     * @param homeAddress 车位联系人住址
     * @param mobNumber   车位负责人联系方式
     * @param parkNo      车场车位编号	 非必填
     * @param appid
     * @param paramMap    扩展预留参数
     * @return 车主第三方ID
     */
    String addPerson(String parkKey, String userName, String carTypeNo, String homeAddress, String mobNumber, String parkNo, String machineNo,String appid, String appSecret, Map<String, Object> paramMap);

    /**
     * 添加车辆
     *
     * @param parkKey  车场ID
     * @param carNo    车牌号
     * @param userNo   用户第三方编号
     * @param appid    appid
     * @param paramMap 扩展预留参数
     * @return
     */
    boolean addCar(String parkKey, String carNo, String userNo, String appid, String appSecret, Map<String, Object> paramMap);

    /**
     * 注销车辆
     *
     * @param parkKey  车场ID
     * @param carNo    车牌号
     * @param appid
     * @param paramMap 扩展预留参数
     * @return
     */
    boolean removeMthCar(String parkKey, String carNo, String appid, String appSecret, Map<String, Object> paramMap);


    /**
     * 延期与充值 按时间段
     *
     * @param parkKey        车场ID
     * @param carNo          车牌号
     * @param mthChargeMoney 充值金额
     * @param beginTime      开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime        结束时间 yyyy-MM-dd HH:mm:ss
     * @param appid
     * @param paramMap       扩展预留参数
     * @return
     */
    boolean extraDate(String parkKey, String carNo, String mthChargeMoney, String beginTime, String endTime, String appid, String appSecret, Map<String, Object> paramMap);

    /**
     * 根据订单号查询停车缴费记录
     *
     * @param parkKey  车场ID
     * @param orderNo  停车订单号
     * @param appid
     * @param paramMap 扩展预留参数
     * @return
     */
    JSONObject getTmpPayRecords(String parkKey, String orderNo, String appid, String appSecret, Map<String, Object> paramMap);

    /**
     * 获取车道列表
     *
     * @param parkKey  车场ID
     * @param appid
     * @param paramMap 扩展预留参数
     * @return
     */
    JSONArray getLaneList(String parkKey, String appid, String appSecret, Map<String, Object> paramMap);

    /**
     * 获取车场信息
     *
     * @param parkKey
     * @param appid
     * @param appSecret
     * @param paramMap
     * @return
     */
    JSONObject getParkInfo(String parkKey, String appid, String appSecret, Map<String, Object> paramMap);


}
