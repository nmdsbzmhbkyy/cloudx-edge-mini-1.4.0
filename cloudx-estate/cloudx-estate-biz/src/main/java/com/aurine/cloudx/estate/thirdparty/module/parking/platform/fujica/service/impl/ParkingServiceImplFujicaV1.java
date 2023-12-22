package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.ParkingLaneConstant;
import com.aurine.cloudx.estate.constant.ParkingLaneStatusConstant;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.ParkingService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.CardTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.FujicaConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.remote.factory.FujicaRemoteParkingServiceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>车场对接，富士业务实现</p>
 *
 * @ClassName: ParkingServiceImplFujica
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-24 8:33
 * @Copyright:
 */
@Service
@Slf4j
public class ParkingServiceImplFujicaV1 implements ParkingService {
    @Autowired
    ProjectParkingPlaceService projectParkingPlaceService;
    @Autowired
    ProjectParkingInfoService projectParkingInfoService;
    @Autowired
    ProjectCarInfoService projectCarInfoService;
    @Autowired
    ProjectParkBillingRuleService projectParkBillingRuleService;
    @Autowired
    ProjectPersonInfoService projectPersonInfoService;
    @Autowired
    ProjectParkBillingInfoService projectParkBillingInfoService;
    @Resource
    ProjectEntryExitLaneService projectEntryExitLaneService;


//    ProjectParkRegionService projectParkRegionService;

    /**
     * 添加车辆
     *
     * @param carRegister
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCar(ProjectParCarRegister carRegister) {
        //1.初始化数据
        ProjectParkingPlace parkingPlace = projectParkingPlaceService.getById(carRegister.getParkPlaceId());//车位
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场
        ProjectCarInfo carInfo = projectCarInfoService.getCarByPlateNumber(carRegister.getPlateNumber());//车辆
        ProjectParkBillingRule billingRule = projectParkBillingRuleService
                .getById(StrUtil.isNotEmpty(parkingPlace.getRuleId()) ? parkingPlace.getRuleId() : carRegister.getRuleId());//规则
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(carInfo.getPersonId());//车主
        String personCode = "";


        VersionEnum version = VersionEnum.V1;


        //获取全部机位号
        List<ProjectEntryExitLane> laneList = projectEntryExitLaneService.list(new QueryWrapper<ProjectEntryExitLane>().lambda().in(ProjectEntryExitLane::getParkId, parkingInfo.getParkId()));
        List<String> machineNoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(laneList)) {
            for (ProjectEntryExitLane lane : laneList) {
                machineNoList.add(lane.getMachineNo());
            }
        } else {
            throw new RuntimeException("添加失败，请在车场管理中更新车道");
        }

        //获取默认的组织机构
        JSONObject orgJson = FujicaRemoteParkingServiceFactory.getInstance(version).getDefaultOrg(parkingInfo.getParkCode(), FujicaConstant.APP_ID, parkingInfo.getProjectId(), null);

        if (orgJson == null) {
            log.error("[富士车场] 未获取到车场{} 的默认组织机构", parkingInfo.getParkCode());
            throw new RuntimeException("添加车辆失败，请联系管理员");
        }

        JSONObject defaultOrjJson = orgJson.getJSONObject("Model");

        if (defaultOrjJson == null) {
            log.error("[富士车场] 未获取到车场{} 的默认组织机构", parkingInfo.getParkCode());
            throw new RuntimeException("添加车辆失败，请联系管理员");
        }
        String defaultOrgGid = defaultOrjJson.getString("Gid");


        //获取人员信息编号： S+车主的SEQ
        String staffNo = "SI" + personInfo.getSeq();
        String staffName = personInfo.getPersonName();


        boolean result = FujicaRemoteParkingServiceFactory.getInstance(version).addCar(
                parkingInfo.getParkCode(),
                FujicaConstant.APP_ID,
                parkingInfo.getProjectId(),
                machineNoList,
                carInfo.getPlateNumber(),
                staffNo,
                staffName,
                personInfo.getTelephone(),
                defaultOrgGid,
                billingRule.getRuleCode(),
                DateUtil.parseLocalDateTime(carRegister.getStartTime().toString() + " 00:00:00"),
                DateUtil.parseLocalDateTime(carRegister.getEndTime().toString() + " 23:59:59"),
                0.0,
                parkingPlace.getPlaceName(),
                null
        );

        if (!result) {
            throw new RuntimeException("登记车辆失败，请检查车场配置！");
        }

        return true;
    }


    /**
     * 注销车辆
     *
     * @param carRegister 车牌注册信息
     * @return
     */
    @Override
    public boolean removeCar(ProjectParCarRegister carRegister) {
        //1.初始化数据
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场

        VersionEnum version = VersionEnum.V1;

        boolean result = FujicaRemoteParkingServiceFactory.getInstance(version).removeMthCar(
                parkingInfo.getParkCode(),
                FujicaConstant.APP_ID,
                parkingInfo.getProjectId(),
                carRegister.getPlateNumber(),
                null
        );

        if (!result) {
            throw new RuntimeException("注销车辆失败！");
        }

        return true;
    }

    /**
     * 延期与充值
     *
     * @param plateNumber
     * @param chargeMoney
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public boolean extraDate(ProjectParCarRegister carRegister, String plateNumber, BigDecimal chargeMoney, LocalDateTime startDate, LocalDateTime endDate) {
        //1.初始化数据
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场

        VersionEnum version = VersionEnum.V1;

        boolean result = FujicaRemoteParkingServiceFactory.getInstance(version).extraDate(
                parkingInfo.getParkCode(), FujicaConstant.APP_ID, parkingInfo.getProjectId(),
                chargeMoney.doubleValue(),
                startDate,
                endDate,
                StrUtil.cleanBlank(carRegister.getPlateNumber()),
                null
        );

        if (!result) {
            throw new RuntimeException("操作失败，请联系管理员");
        }

        return true;
    }

    /**
     * 获取临时车缴费记录
     *
     * @param parkingId
     * @param orderNo
     * @return
     */
    @Override
    public boolean getTmpPayRecords(String parkingId, String orderNo) {
        return true;
    }

    /**
     * 获取当前停车场的车道数据
     *
     * @param parkingId
     * @return
     */
    @Override
    public List<ProjectEntryExitLane> getLaneList(String parkingId) {
        //1.初始化数据
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);//车场

        VersionEnum version = VersionEnum.V1;
        JSONArray resultJsonArray = FujicaRemoteParkingServiceFactory.getInstance(version).getLaneCameraList(parkingInfo.getParkCode(), FujicaConstant.APP_ID, parkingInfo.getProjectId(), null);


        if (resultJsonArray == null) {
            throw new RuntimeException("未获取到车道数据");
        }


        /**
         * [
         *         {
         *             "ID": 1,
         *             "Name": "入口车道(一)识别设备",
         *             "Ip": "10.110.22.161",
         *             "Port": 30000,
         *             "UserId": "admin",
         *             "Password": "admin",
         *             "DeviceCategory": "2BEF5026-E894-400D-A8E1-8E4D4B577D5C",
         *             "LaneId": "52F51E6D-815C-4B23-AD68-75F6AA70832F",
         *             "Remark": "",
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "hasExtendedfields": 0,
         *             "Rid": "0d49c2aa-8bc5-4f91-aae6-d9528206eb5f",
         *             "ConfigVersion": "ff2e93cb-0dd0-4b2d-ae7c-96b46ba1d538",
         *             "MacAddress": "90-70-66-0F-56-71",
         *             "Reserv1": 0,
         *             "Reserv2": "90-70-66-0F-56-71",
         *             "Reserv3": ""
         *         }
         *     ]
         *
         *
         */

        List<JSONObject> jsonList = resultJsonArray.toJavaList(JSONObject.class);

        List<ProjectEntryExitLane> laneList = new ArrayList<>();
        ProjectEntryExitLane lane;
        for (JSONObject laneJson : jsonList) {
            lane = new ProjectEntryExitLane();
            lane.setLaneCode(laneJson.getString("LaneId"));
            lane.setLaneName(laneJson.getString("Name"));
            lane.setDirection("1".equals(laneJson.getString("Reserv1")) ? ParkingLaneConstant.LANE_IN : ParkingLaneConstant.LANE_OUT);
            lane.setMachineNo(laneJson.getString("ID"));
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

        //1.初始化数据
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);//车场


        //数据清洗成对接格式


        JSONObject resultJsonObject = FujicaRemoteParkingServiceFactory.getInstance(VersionEnum.V1).getParkInfo(
                parkingInfo.getParkCode(),
                FujicaConstant.APP_ID,
                parkingInfo.getProjectId(),
                null
        );


        if (resultJsonObject == null) {
            throw new RuntimeException("未获取到车场数据，请检查配置是否正确");
        }

        String payImgUrl = resultJsonObject.getString("parkQRcodeUrl");
        return payImgUrl;
    }

    /**
     * 初始化支付类型
     *
     * @param parkId
     */
    @Override
    public List<ProjectParkBillingRule> getBillingRuleList(String parkId, Integer projectId) {
        List<ProjectParkBillingRule> parkBillingRuleList = new ArrayList<>();
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkId);//车场

        JSONObject resultJson = FujicaRemoteParkingServiceFactory.getInstance(VersionEnum.V1).getPayType(parkingInfo.getParkCode(), FujicaConstant.APP_ID, parkingInfo.getProjectId(), null);

        if (resultJson == null) {
            log.error("[富士车场] 未获取到缴费规则数据");
            throw new RuntimeException("未获取到缴费规则数据");
        }

        JSONArray resultArray = resultJson.getJSONArray("Records");
        if (CollUtil.isEmpty(resultArray)) {
            log.error("[富士车场] 未获取到缴费规则数据");
            throw new RuntimeException("未获取到缴费规则数据");
        }

        /**
         *    {
         *             "ID": 3,
         *             "Code": "22",
         *             "Name": "贵宾卡A",
         *             "TcmType": 1,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "C33E7736-9FB3-4932-B4CC-030FAA76EBFC"
         *         },
         *         {
         *             "ID": 4,
         *             "Code": "23",
         *             "Name": "贵宾卡B",
         *             "TcmType": 1,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "1E0667B0-90D7-46A9-8A68-50D7E4673759"
         *         },
         *         {
         *             "ID": 5,
         *             "Code": "24",
         *             "Name": "月租卡A",
         *             "TcmType": 2,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "C97611CE-C6EA-4582-9680-55934B79F9F7"
         *         },
         *         {
         *             "ID": 6,
         *             "Code": "25",
         *             "Name": "月租卡B",
         *             "TcmType": 2,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "2882BCD7-44A2-41A1-BE7D-F2C1304DBF88"
         *         },
         *         {
         *             "ID": 7,
         *             "Code": "26",
         *             "Name": "月租卡C",
         *             "TcmType": 2,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "A4807A31-2DAA-462C-BC27-FAC3C2044720"
         *         },
         *         {
         *             "ID": 8,
         *             "Code": "27",
         *             "Name": "月租卡D",
         *             "TcmType": 2,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "6CCF9944-A068-4B65-B4C4-3F227CC7671D"
         *         },
         *         {
         *             "ID": 13,
         *             "Code": "2C",
         *             "Name": "时租卡A",
         *             "TcmType": 4,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "77258425-1621-4177-BBAA-73E97FD00C3D"
         *         },
         *         {
         *             "ID": 14,
         *             "Code": "2D",
         *             "Name": "时租卡B",
         *             "TcmType": 4,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "BC1AB885-5593-4ECA-AF33-A2BAE0B51566"
         *         },
         *         {
         *             "ID": 15,
         *             "Code": "2E",
         *             "Name": "时租卡C",
         *             "TcmType": 4,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "8763140D-A540-4C3F-9DC4-1F42EDD13E12"
         *         },
         *         {
         *             "ID": 16,
         *             "Code": "2F",
         *             "Name": "时租卡D",
         *             "TcmType": 4,
         *             "SubSystem": 1,
         *             "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *             "Rid": "F6846F2D-EA74-431D-A52C-CD4DD442DF1D"
         *         }
         */
        boolean haveFreeRule = false;
        for (JSONObject recode : resultArray.toJavaList(JSONObject.class)) {

            //仅存储首个免费车
            if (haveFreeRule && CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")) == ParkingRuleTypeEnum.FREE) {
                continue;
            }

            if (CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")) == ParkingRuleTypeEnum.FREE) {
                haveFreeRule = true;
            }

            parkBillingRuleList.add(createBean(recode.getString("Rid"), recode.getString("Name"), parkId, CardTypeEnum.getCloudCodeByFujica(recode.getString("TcmType")), projectId));
        }

        return parkBillingRuleList;
    }


    /**
     * 构造停车收费规则
     *
     * @param ruleCode
     * @param ruleName
     * @return
     */
    private ProjectParkBillingRule createBean(String ruleCode, String ruleName, String parkId, ParkingRuleTypeEnum ruleTypeEnum, Integer projectId) {
        ProjectParkBillingRule rule = new ProjectParkBillingRule();
        rule.setRuleCode(ruleCode);
        rule.setRuleName(ruleName);
        rule.setRuleName(ruleName);
        rule.setIsDisable(ruleTypeEnum == ParkingRuleTypeEnum.MONTH ? "1" : "0");
        rule.setProjectId(projectId);
        rule.setParkId(parkId);
        rule.setRuleType(ruleTypeEnum.code);
        return rule;
    }

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
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return ParkingAPICompanyEnum.FUJICA.name();
    }
}
