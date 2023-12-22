package com.aurine.cloudx.estate.thirdparty.module.parking.platform.reformer.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.ParkingLaneConstant;
import com.aurine.cloudx.estate.constant.ParkingLaneStatusConstant;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.ParkingService;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote.factory.AliRemoteParkingServiceFactory;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.util.AliEdgeProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-06-09 11:55
 */
@Slf4j
@Service
public class ParkingServiceImplReformerV1 implements ParkingService {


    @Autowired
    ProjectParkingInfoService projectParkingInfoService;
    @Autowired
    ProjectCarInfoService projectCarInfoService;
    @Autowired
    ProjectPersonInfoService projectPersonInfoService;
    @Autowired
    AliEdgeProjectUtil aliEdgeProjectUtil;



    @Override
    public boolean addCar(ProjectParCarRegister carRegister) {

        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(carRegister.getPlateNumber());//车辆
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(carInfo.getPersonId());//车主

        ArrayList<String> plateNumberList = new ArrayList<>();
        String plateNumber = carRegister.getPlateNumber();
        plateNumberList.add(plateNumber);

        String aliEdgeProject = aliEdgeProjectUtil.getAliEdgeProject(parkingInfo.getProjectId());

        if (!StrUtil.hasEmpty(carRegister.getParkId()) && !plateNumberList.isEmpty() &&  !StrUtil.hasEmpty(aliEdgeProject)){
            Boolean result = AliRemoteParkingServiceFactory.getInstance(VersionEnum.getByCode(getVersion())).addCar(
                    plateNumberList,
                    carRegister.getParkId(),
                    aliEdgeProject,
                    personInfo.getCredentialNo(),
                    personInfo.getPersonName(),
                    personInfo.getTelephone());
           if (result){
               this.extraDate1(
                       carRegister,
                       plateNumber,
                       null,
                       DateUtil.parseLocalDateTime(carRegister.getStartTime().toString() + " 00:00:00"),
                       DateUtil.parseLocalDateTime(carRegister.getEndTime().toString() + " 23:59:59"));
           }else{
               log.error("[立方车场] 车辆添加失败");
           }

        }else {
            throw new RuntimeException("错误");
        }

        return false;
    }


    @Override
    public boolean removeCar(ProjectParCarRegister carRegister) {

        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场
        String aliEdgeProject = aliEdgeProjectUtil.getAliEdgeProject(parkingInfo.getProjectId());
        if (StrUtil.hasEmpty(carRegister.getParkId()) && StrUtil.hasEmpty(carRegister.getPlateNumber()) && StrUtil.hasEmpty(aliEdgeProject)){
            throw new RuntimeException("错误");
        }else {
            JSONObject jsonObject = AliRemoteParkingServiceFactory.getInstance(VersionEnum.getByCode(getVersion())).removeCar(
                    carRegister.getParkId(),
                    carRegister.getPlateNumber(),
                    aliEdgeProject);

        }

        return false;
    }

    /**
     * 充值
     * @param carRegister
     * @param plateNumber
     * @param chargeMoney
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean extraDate1(ProjectParCarRegister carRegister, String plateNumber, BigDecimal chargeMoney, LocalDateTime startDate, LocalDateTime endDate) {


        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());
        String aliEdgeProject = aliEdgeProjectUtil.getAliEdgeProject(parkingInfo.getProjectId());
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(carRegister.getPlateNumber());//车辆

        //获取闸道列表
        List<ProjectEntryExitLane> laneList = this.getLaneList(carRegister.getParkId());
        List<String> barrierListId = aliEdgeProjectUtil.getBarrierListId(laneList);
//        Boolean updateAndAdd = true;//判断是新增还是延期
//        if (carInfo == null){
//            updateAndAdd = true;
//        }else {
//            updateAndAdd = false;
//        }
        JSONObject jsonObject1 = AliRemoteParkingServiceFactory.getInstance(VersionEnum.getByCode(getVersion())).permissionBarrier(
                barrierListId,
                startDate.toString(),
                endDate.toString(),
                carRegister.getParkId(),
                carRegister.getPlateNumber(),
                aliEdgeProject);
        return false;
    }
    /**
     * 延期
     *
     * @param plateNumber
     * @param chargeMoney
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public boolean extraDate(ProjectParCarRegister carRegister, String plateNumber, BigDecimal chargeMoney, LocalDateTime startDate, LocalDateTime endDate) {


        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());
        String aliEdgeProject = aliEdgeProjectUtil.getAliEdgeProject(parkingInfo.getProjectId());
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(carRegister.getPlateNumber());//车辆

        //获取闸道列表
        List<ProjectEntryExitLane> laneList = this.getLaneList(carRegister.getParkId());
        List<String> barrierListId = aliEdgeProjectUtil.getBarrierListId(laneList);
//        Boolean updateAndAdd = true;//判断是新增还是延期
//        if (carInfo == null){
//            updateAndAdd = true;
//        }else {
//            updateAndAdd = false;
//        }
        JSONObject jsonObject1 = AliRemoteParkingServiceFactory.getInstance(VersionEnum.getByCode(getVersion())).updatePermissionBarrier(
                barrierListId,
                startDate.toString(),
                endDate.toString(),
                carRegister.getParkId(),
                carRegister.getPlateNumber(),
                aliEdgeProject);
        return false;
    }

    /**
     * 获取临时车缴费记录
     *
     * @param orderNo
     * @param parkingId
     * @return
     */
    @Override
    public boolean getTmpPayRecords(String parkingId, String orderNo) {
        return false;
    }

    /**
     * 获取当前停车场的车道数据
     *
     * @param parkingId
     * @return
     */
    @Override
    public List<ProjectEntryExitLane> getLaneList(String parkingId) {

        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);//车场
        String aliEdgeProject = aliEdgeProjectUtil.getAliEdgeProject(parkingInfo.getProjectId());

        JSONArray resultJsonArray = AliRemoteParkingServiceFactory.getInstance(VersionEnum.getByCode(getVersion())).getParkingBarrierList(parkingInfo.getParkCode(),aliEdgeProject);

        if (resultJsonArray == null) {
            throw new RuntimeException("未获取到车道数据");
        }

        List<JSONObject> jsonList = resultJsonArray.toJavaList(JSONObject.class);

        List<ProjectEntryExitLane> laneList = new ArrayList<>();
        ProjectEntryExitLane lane;
        for (JSONObject laneJson : jsonList) {
            lane = new ProjectEntryExitLane();
            lane.setLaneCode(laneJson.getString("barrierId"));
            lane.setLaneName(laneJson.getString("barrierName"));
            lane.setDirection("IN".equals(laneJson.getString("direction")) ? ParkingLaneConstant.LANE_IN : ParkingLaneConstant.LANE_OUT);
            lane.setStatus(ParkingLaneStatusConstant.USING);
            lane.setParkId(parkingId);
            laneList.add(lane);
        }


        return laneList;
    }
    /**
     * 获取车场支付URL
     *
     * @param parkingId
     * @return
     */
    @Override
    public String getParkingPayUrl(String parkingId) {
        return null;
    }

    /**
     *  获取支付类型列表
     * @param parkingId
     */
    @Override
    public List<ProjectParkBillingRule> getBillingRuleList(String parkingId, Integer projectId) {
        return null;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    @Override
    public String getPlatform() {
        return ParkingAPICompanyEnum.REFORMER.name();
    }
}
