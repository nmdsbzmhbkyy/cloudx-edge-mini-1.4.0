package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.SfirmConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.core.SfirmDataConnector;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.remote.SfirmRemoteParkingService;
import com.aurine.cloudx.estate.thirdparty.module.parking.util.SfirmUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/22
 * @Copyright:
 */
@Service
@Slf4j
public class SfirmRemoteParkingServiceV1Impl implements SfirmRemoteParkingService {

    @Resource
    private SfirmDataConnector sfirmDataConnector;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


    /**
     * 添加车主
     *
     * @param parkKey     车场ID
     * @param userName    车主名
     * @param carTypeNo   车辆类型编码	收费规则
     * @param homeAddress 车位联系人住址
     * @param mobNumber   车位负责人联系方式
     * @param parkNo      车场车位编号	 非必填
     * @param machineNo   机位编号，一共32位 二进制数，0和1，1表示使用，0表示不使用，每4位转换为16进制数，最终位6位HEX
     * @param appid
     * @param paramMap    扩展预留参数
     * @return
     */
    @Override
    public String addPerson(String parkKey, String userName, String carTypeNo, String homeAddress, String mobNumber, String parkNo, String machineNo, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);
        requestJson.put("userName", userName);
        requestJson.put("carTypeNo", carTypeNo);
        requestJson.put("homeAddress", homeAddress);
        requestJson.put("mobNumber", mobNumber);
        requestJson.put("parkNo", parkNo);

        if (StringUtils.isNotEmpty(machineNo)) {
            requestJson.put("machineNo", machineNo);
        }

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inform/AddPerson", requestJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return respJson.getJSONObject("data").getString("userNo");
        } else {
            if (StringUtils.equals(respJson.getString("msg"), "手机号码格式错误")) {
                throw new RuntimeException("手机号码格式错误");
            }
        }
        return "";
    }

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
    @Override
    public boolean addCar(String parkKey, String carNo, String userNo, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);
        requestJson.put("carNo", carNo);
        requestJson.put("userNo", userNo);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inform/AddMthCar", requestJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return true;
        } else {
            if (StringUtils.equals(respJson.getString("msg"), "月租车已存在")) {
                throw new RuntimeException("车牌号已存在");
            }
            return false;
        }
    }

    /**
     * 注销车辆
     *
     * @param parkKey  车场ID
     * @param carNo    车牌号
     * @param appid
     * @param paramMap 扩展预留参数
     * @return
     */
    @Override
    public boolean removeMthCar(String parkKey, String carNo, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);
        requestJson.put("carNo", carNo);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inform/FailMonthCar", requestJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return true;
        } else {
            if (StringUtils.equals(respJson.getString("msg"), "月租车不存在")) {//对方平台月租车不存在的情况下，直接删除车辆
                return true;
            }
            return false;
        }
    }

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
    @Override
    public boolean extraDate(String parkKey, String carNo, String mthChargeMoney, String beginTime, String endTime, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);
        requestJson.put("carNo", carNo);
        requestJson.put("mthChargeMoney", mthChargeMoney);
        requestJson.put("beginTime", beginTime);
        requestJson.put("endTime", endTime);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inform/ChargeMonthCar", requestJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据订单号查询停车缴费记录
     *
     * @param parkKey   车场ID
     * @param orderNo   停车订单号
     * @param appid
     * @param appSecret
     * @param paramMap  扩展预留参数
     * @return
     */
    @Override
    public JSONObject getTmpPayRecords(String parkKey, String orderNo, String appid, String appSecret, Map<String, Object> paramMap) {

        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);
        requestJson.put("orderNo", orderNo);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inquire/GetTmpPayRecords", requestJson);
        log.info("获取到缴费记录：{}", respJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return respJson.getJSONArray("data").getJSONObject(0);
//            return respJson.getJSONObject("data");
        } else {
            return null;
        }
    }

    /**
     * 获取车道列表
     *
     * @param parkKey   车场ID
     * @param appid
     * @param appSecret
     * @param paramMap  扩展预留参数
     * @return
     */
    @Override
    public JSONArray getLaneList(String parkKey, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inquire/GetVehicleInfo", requestJson);
        log.info("获取到车道信息：{}", respJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return respJson.getJSONArray("data");
//            return respJson.getJSONObject("data");
        } else {
            return null;
        }
    }

    /**
     * 获取车场信息
     *
     * @param parkKey
     * @param appid
     * @param appSecret
     * @param paramMap
     * @return
     */
    @Override
    public JSONObject getParkInfo(String parkKey, String appid, String appSecret, Map<String, Object> paramMap) {
        //拼接传输请求头
        JSONObject requestJson = new JSONObject();
        requestJson.put("parkKey", parkKey);

        requestJson.put("version", "v1.0");
        requestJson.put("appid", appid);
        requestJson.put("rand", SfirmUtil.random());
        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));


        JSONObject respJson = sfirmDataConnector.post(SfirmConstant.baseUrl + "/Api/Inquire/GetParkInfo", requestJson);
        log.info("获取到车场信息：{}", respJson);
        if (StringUtils.equals(respJson.getString("code"), "1")) {
            return respJson.getJSONObject("data");
//            return respJson.getJSONObject("data");
        } else {
            return null;
        }
    }
}
