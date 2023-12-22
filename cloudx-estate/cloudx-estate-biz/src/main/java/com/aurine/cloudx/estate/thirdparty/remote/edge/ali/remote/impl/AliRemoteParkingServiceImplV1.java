package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.core.AliEdgeDataConnector;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.AliRemoteParkingService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util.AliEdgeRespondUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-10 9:09
 * @Copyright:
 */
@Service
@Slf4j
public class AliRemoteParkingServiceImplV1 implements AliRemoteParkingService {
    @Resource
    private AliEdgeDataConnector aliEdgeDataConnector;

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
     * 添加车辆
     * @param areaId 车牌号
     * @param parkingLotId 停车场id
     * @param projectId 项目id
     * @param userId 车主证件号
     * @param userName 车主名称
     * @param userPhone 车主手机号
     * @return
     */
    @Override
    public  Boolean addCar(ArrayList<String> areaId, String parkingLotId, String projectId, String userId, String userName, String userPhone) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("areaId",areaId);

        requestJson.put("parkingLotId", parkingLotId);
        requestJson.put("projectId", projectId);
        requestJson.put("userName", userName);
        requestJson.put("userPhone", userPhone);

//        JSONObject respJson = aliEdgeDataConnector.post("/v1/sinking/parking/barrier/list", requestJson);
//        log.info("[阿里边缘] 新增车辆信息：{}", respJson);
//        AliEdgeRespondUtil.handle(respJson);



        return true;

    }

    /**
     *  删除车辆
     * @param parkingLotId 停车场id
     * @param plateNumber 车牌号
     * @param projectId  项目id
     * @return
     */
    @Override
    public  JSONObject removeCar(String parkingLotId, String plateNumber, String projectId) {
        Map<String,Object> queryParamMap = new HashMap<>();

        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        queryParamMap.put("plateNumber",plateNumber);
//        JSONObject respJson = aliEdgeDataConnector.get("parking/vehicle", queryParamMap);
//        log.info("[阿里边缘] 删除车辆信息：{}", respJson);
//        AliEdgeRespondUtil.handle(respJson);

        return null;


    }
    /**
     * 更新车辆
     * @param areaId 车牌号
     * @param parkingLotId 停车场id
     * @param projectId 项目id
     * @param userId 车主证件号
     * @param userName 车主名称
     * @param userPhone 车主手机号
     * @return
     */
    @Override
    public  JSONObject updateCar(ArrayList<String> areaId, String parkingLotId, String projectId, String userId, String userName, String userPhone) {

        JSONObject requestJson = new JSONObject();
        requestJson.put("areaId",areaId);

        requestJson.put("parkingLotId", parkingLotId);
        requestJson.put("projectId", projectId);
        requestJson.put("userName", userName);
        requestJson.put("userPhone", userPhone);

        JSONObject respJson = aliEdgeDataConnector.put("/v1/sinking/parking/vehicle", requestJson);
        log.info("[阿里边缘] 更新车辆信息：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");
    }

    /**
     * 获取车场信息
     *
     * @param projectId
     * @return
     */
    @Override

    public JSONObject getParkInfo(String projectId) {
        //拼接传输请信息
//        JSONObject requestJson = new JSONObject();
//        requestJson.put("parkKey", parkKey);
//
//        requestJson.put("version", "v1.0");
//        requestJson.put("appid", appid);
//        requestJson.put("rand", SfirmUtil.random());
//        requestJson.put("sign", SfirmUtil.signGenerator(requestJson, appSecret));
        Map<String,Object> queryParamMap = new HashMap<>();
        projectId = "a124zqUIBWbYpNmB";
        queryParamMap.put("projectId",projectId);
        JSONObject respJson = aliEdgeDataConnector.get("/v1/sinking/parking", queryParamMap);
        log.info("[阿里边缘] 获取停车场区域信息：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");

    }
    /**
     * 获取停车场区域信息
     * @param areaId  区域id
     * @param parkingLotId  停车场id
     * @param projectId
     * @return
     */
    @Override
    public JSONObject getParkingArea(String areaId, String parkingLotId, String projectId) {
        Map<String,Object> queryParamMap = new HashMap<>();



        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        queryParamMap.put("areaId",areaId);
        JSONObject respJson = aliEdgeDataConnector.get("/v1/sinking/parking", queryParamMap);
        log.info("[阿里边缘] 获取到车场信息：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");
    }
    /**
     *  停车场区域下道闸列表信息查询
     * @param areaId
     * @param parkingLotId
     * @param projectId
     * @return
     */
    @Override
    public JSONObject getParkingAreaBarrier(String areaId, String parkingLotId, String projectId) {
        Map<String,Object> queryParamMap = new HashMap<>();



        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        queryParamMap.put("areaId",areaId);
        JSONObject respJson = aliEdgeDataConnector.get("/v1/sinking/parking/area/barrier", queryParamMap);
        log.info("[阿里边缘] 停车场区域下道闸列表信息查询：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");
    }
    /**
     * 停车场区域列表信息查询
     * @param parkingLotId
     * @param projectId
     * @return
     */
    @Override
    public JSONObject getParkingAreaList(String parkingLotId, String projectId) {
        Map<String,Object> queryParamMap = new HashMap<>();



        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        JSONObject respJson = aliEdgeDataConnector.get("/v1/sinking/parking/area/list", queryParamMap);
        log.info("[阿里边缘] 停车场区域列表信息查询：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");
    }
    /**
     * 查询区域车位列表
     * @param areaId
     * @param parkingLotId
     * @param projectId
     * @return
     */
    @Override
    public JSONObject getParkingAreaParkSpace(String areaId, String parkingLotId, String projectId) {
        Map<String,Object> queryParamMap = new HashMap<>();



        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        queryParamMap.put("areaId",areaId);
        JSONObject respJson = aliEdgeDataConnector.get("/v1/sinking/parking/area/parkspace", queryParamMap);
        log.info("[阿里边缘] 查询区域车位列表：{}", respJson);
        AliEdgeRespondUtil.handle(respJson);

        return respJson.getJSONObject("data");
    }

    /**
     * 停车场道闸列表信息查询
     * @param parkingLotId 停车场id
     * @param projectId
     * @return
     */
    @Override
    public JSONArray getParkingBarrierList(String parkingLotId, String projectId) {

        Map<String,Object> queryParamMap = new HashMap<>();
        queryParamMap.put("projectId",projectId);
        queryParamMap.put("parkingLotId",parkingLotId);
        JSONObject respJson = aliEdgeDataConnector.get("parking/barrier/list", queryParamMap);
        log.info("[阿里边缘] 停车场下道闸列表信息查询：{}", respJson);
//        AliEdgeRespondUtil.handle(respJson);
        JSONArray data = respJson.getJSONArray("body");

        return data;
    }

    /**
     * 车辆对道闸授权
     * @param barrierList 道闸列表
     * @param effectiveDate 开始时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param expiryDate 终止时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param parkingLotId  停车场id
     * @param plateNumber 车牌号
     * @param projectId
     * @return
     */
    @Override
    public JSONObject permissionBarrier(List<String> barrierList, String effectiveDate, String expiryDate, String parkingLotId, String plateNumber, String projectId) {

        JSONObject requestJson = new JSONObject();
        requestJson.put("barrierList",barrierList);

        requestJson.put("effectiveDate",effectiveDate);
        requestJson.put("expiryDate",expiryDate);
        requestJson.put("parkingLotId",parkingLotId);
        requestJson.put("plateNumber", plateNumber);
        requestJson.put("projectId",projectId);

            //新增
//            JSONObject respJson = aliEdgeDataConnector.post("parking/vehicle/permission/barrier", requestJson);
//            log.info("[阿里边缘] 车辆对道闸授权信息：{}", respJson);
//            AliEdgeRespondUtil.handle(respJson);

        JSONObject respJson = new JSONObject();
        return respJson.getJSONObject("data");


    }
    /**
     * 车辆对道闸授权更新
     * @param barrierList 道闸列表
     * @param effectiveDate 开始时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param expiryDate 终止时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param parkingLotId  停车场id
     * @param plateNumber 车牌号
     * @param projectId
     * @return
     */
    @Override
    public JSONObject updatePermissionBarrier(List<String> barrierList, String effectiveDate, String expiryDate, String parkingLotId, String plateNumber, String projectId) {

        JSONObject requestJson = new JSONObject();
        requestJson.put("barrierList",barrierList);

        requestJson.put("effectiveDate",effectiveDate);
        requestJson.put("expiryDate",expiryDate);
        requestJson.put("parkingLotId",parkingLotId);
        requestJson.put("plateNumber", plateNumber);
        requestJson.put("projectId",projectId);

        //新增
//        JSONObject respJson = aliEdgeDataConnector.put("parking/vehicle/permission/barrier", requestJson);
//        log.info("[阿里边缘] 车辆对道闸授权信息：{}", respJson);
//        AliEdgeRespondUtil.handle(respJson);

        JSONObject respJson = new JSONObject();
        return respJson.getJSONObject("data");

    }
}
