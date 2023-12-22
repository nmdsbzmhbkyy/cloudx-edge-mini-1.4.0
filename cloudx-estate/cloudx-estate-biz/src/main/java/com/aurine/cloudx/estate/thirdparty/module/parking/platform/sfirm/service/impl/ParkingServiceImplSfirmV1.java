package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.ParkingLaneConstant;
import com.aurine.cloudx.estate.constant.ParkingLaneStatusConstant;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PayTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.ParkingService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.SfirmConstant;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.remote.factory.SfirmRemoteParkingServiceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>车场对接，赛菲姆业务实现</p>
 *
 * @ClassName: ParkingServiceImplSfirm
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-24 8:33
 * @Copyright:
 */
@Service
public class ParkingServiceImplSfirmV1 implements ParkingService {
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

        /**
         * @author: 王伟
         * @since 2020-08-08
         */
        //获取第一个已激活的月租车规则
//        List<ProjectParkBillingRule> ruleList = projectParkBillingRuleService.list(new QueryWrapper<ProjectParkBillingRule>().lambda()
//                .eq(ProjectParkBillingRule::getProjectId, billingRule.getProjectId())
//                .eq(ProjectParkBillingRule::getRuleType, CarTypeEnum.MONTH_A.type)
//                .eq(ProjectParkBillingRule::getIsDisable, "0"));
//
//        if (billingRule.getRuleType().equalsIgnoreCase(CarTypeEnum.FREE.type) && CollectionUtil.isNotEmpty(ruleList)) {
//            billingRule = ruleList.get(0);
//        }

        VersionEnum version = VersionEnum.V1;

        /**
         * 根据当前生成机位号
         * @author:王伟
         * @since 2021-02-05
         */
        //非空校验
        String machineNo = "";
        if (StringUtils.isNotEmpty(carRegister.getLaneList())) {
            if ("[]".equals(carRegister.getLaneList())) {
                carRegister.setLaneList(null);
            } else {
                List<ProjectEntryExitLane> laneList = new ArrayList<>();
                String[] laneIdArray = carRegister.getLaneList().split(",");
                if (laneIdArray.length >= 1) {
                    laneList = projectEntryExitLaneService.list(new QueryWrapper<ProjectEntryExitLane>().lambda().in(ProjectEntryExitLane::getLaneId, laneIdArray));
                }

                //从车道中获取控制机编号，并将控制机编号转译位传输布尔型数据
                if (CollUtil.isNotEmpty(laneList)) {
                    for (ProjectEntryExitLane lane : laneList) {
                        machineNo = valCtrlNo(Integer.valueOf(lane.getMachineNo().trim()), machineNo);
                    }

                    if (StringUtils.isNotEmpty(machineNo)) {
                        machineNo = this.longBin2Hex(machineNo);//转换为16进制
                    }

                }
            }
        }


        //判断车位是否已经存在车主的第三方编号, 如果存在，不再添加车主（为以后一位多车情况预留）
        if (StringUtils.isEmpty(parkingPlace.getPersonCode())) {

            //添加车主，并返回第三方车主ID
            personCode = SfirmRemoteParkingServiceFactory.getInstance(version).addPerson(
                    parkingInfo.getParkCode(),
                    personInfo.getPersonName(),
                    billingRule.getRuleCode(),
                    "住户地址",
                    personInfo.getTelephone(),
                    carRegister.getParkPlaceName(),
                    machineNo,
                    SfirmConstant.appId,
                    SfirmConstant.appSecrets,
                    null
            );
            if (StringUtils.isEmpty(personCode)) {
                throw new RuntimeException("登记车辆失败，请检查车场配置！");
            }
            parkingPlace.setPersonCode(personCode);
            projectParkingPlaceService.updateById(parkingPlace);
            //等待返回信息，失败情况处理（超时、错误）
        }


        //添加车辆
        boolean result = SfirmRemoteParkingServiceFactory.getInstance(version).addCar(
                parkingInfo.getParkCode(),
                StrUtil.cleanBlank(carInfo.getPlateNumber()),
                parkingPlace.getPersonCode(),
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
                null
        );
        if (!result) {
            throw new RuntimeException("登记车辆失败，请检查车场配置！");
        }

        return true;
    }

    /**
     * 将机位号转换为地址嘛
     *
     * @param no
     * @param code
     * @return
     */
    private String valCtrlNo(Integer no, String code) {
        if (StringUtils.isEmpty(code)) {
            code = "00000000000000000000000000000000";
        }
        char[] charArray = code.toCharArray();

        if (no - 1 >= 0 && no <= charArray.length) {
            charArray[no - 1] = '1';
            code = charArray.toString();
        }

        return code;
    }


    private String longBin2Hex(String bin) {
        int length = bin.length();
        if (length <= 4) {
            return bin2hex(bin);
        }

        StringBuffer stringBuffer = new StringBuffer();
        int i = length - 4;
        for (; i >= 0; i = i - 4) {
            stringBuffer.append(bin2hex(bin.substring(i, i + 4)));
        }
        if (i != -4) {
            i = i + 4;
            stringBuffer.append(bin2hex(bin.substring(0, i)));
        }
        return stringBuffer.reverse().toString();
    }

    private String bin2hex(String binStr) {
        return Integer.toHexString(Integer.parseInt(binStr.trim(), 2));
    }

    private String hex2Bin(String hexStr) {
        return Integer.toBinaryString(Integer.parseInt(hexStr.trim(), 16));
    }

    /**
     * 注销车辆
     *
     * @param carRegister 车牌注册信息
     * @return
     */
    @Override
    public boolean removeCar(ProjectParCarRegister carRegister) {
        //1.初始化数据s
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(carRegister.getParkId());//车场

        VersionEnum version = VersionEnum.V1;

        boolean result = SfirmRemoteParkingServiceFactory.getInstance(version).removeMthCar(
                parkingInfo.getParkCode(),
                StrUtil.cleanBlank(carRegister.getPlateNumber()),
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
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

        boolean result = SfirmRemoteParkingServiceFactory.getInstance(version).extraDate(
                parkingInfo.getParkCode(),
                StrUtil.cleanBlank(carRegister.getPlateNumber()),
                chargeMoney.toString(),
                DateUtil.format(startDate, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.format(endDate, "yyyy-MM-dd HH:mm:ss"),
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
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
        //1.初始化数据
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);//车场

        VersionEnum version = VersionEnum.V1;

        JSONObject resultJson = SfirmRemoteParkingServiceFactory.getInstance(version).getTmpPayRecords(
                parkingInfo.getParkCode(),
                orderNo,
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
                null
        );

        /**
         *  {
         *             "indexId": 0,
         *             "parkKey": "ffayhd1q",
         *             "payOrderNo": "LP201807021028441786014ffayhd1q",
         *             "orderNo": "20180702102717983-ABC123",
         *             "payedSN": "",
         *             "carNo": "粤ABC123",
         *             "payTypeCode": "79001",
         *             "payOrderMoney": "0.00",
         *             "payedTime": "2018-07-02 10:28:44",
         *             "payedMoney": "0.00",
         *             "tempTimeCount": 1,
         *             "carTypeNo": "3651",
         *             "orderTypeNo": "5901",
         *             "payOrderDesc": "",
         *             "couponRecordID": "",
         *             "fpStatus": "0"
         *         }
         */

        if (resultJson == null) {
            return false;
        }
        String payOrderNo = resultJson.getString("payOrderNo"); //第三方支付订单号
        String payTypeCode = resultJson.getString("payTypeCode"); //支付方式，需转义
        String payedTime = resultJson.getString("payedTime"); //支付时间
        LocalDateTime payedDateTime = DateUtil.parseLocalDateTime(payedTime, "yyyy-MM-dd HH:mm:ss"); //支付时间
        String payOrderMoney = resultJson.getString("payOrderMoney"); //订单金额
        BigDecimal payOrderMoneyNum = new BigDecimal(payOrderMoney);
        String payedMoney = resultJson.getString("payedMoney"); //支付金额
        BigDecimal payedMoneyNum = new BigDecimal(payedMoney);

        PayTypeEnum payTypeEnum;
        //支付类型转义
        switch (payTypeCode) {
            case "79001"://线下现金
            case "79002"://平台现金支付
                payTypeEnum = PayTypeEnum.CASH;
                break;
            case "79003"://微信支付
                payTypeEnum = PayTypeEnum.WECHAT;
                break;
            case "79007"://支付宝支付
                payTypeEnum = PayTypeEnum.ALIPAY;
                break;
            case "79010"://线下微信
                payTypeEnum = PayTypeEnum.OFFLINE_WECHAT;
                break;
            case "79011"://线下支付宝
                payTypeEnum = PayTypeEnum.OFFLINE_ALIPAY;
                break;
            case "79004"://Android端支付
            case "79005"://IOS端支付
            case "79006"://终端设备支付
            case "79008"://钱包支付
            case "79009"://第三方支付
            case "79012"://支付宝无感支付
            case "79013"://微信无感支付
            case "79014"://建行支付
            case "79015"://招行一网通支付
            case "79016"://银联无感支付
            case "79017"://建行无感支付
            case "79018"://威富通支付
            case "79019"://招行无感支付
            case "79020"://工行无感支付
            case "79021"://工行支付
            case "79022"://农行无感支付
            case "79023"://农行支付
            case "79024"://ETC支付
            case "79025"://中行支付
            case "79026"://中行无感支付
                payTypeEnum = PayTypeEnum.OTHER;
            default:
                payTypeEnum = PayTypeEnum.OTHER;
                break;
        }

        return projectParkBillingInfoService.addTempCarBill(payTypeEnum, parkingId, orderNo, payOrderMoneyNum, payedMoneyNum, payedDateTime, payOrderNo);
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

        JSONArray resultJsonArray = SfirmRemoteParkingServiceFactory.getInstance(version).getLaneList(
                parkingInfo.getParkCode(),
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
                null
        );


        if (resultJsonArray == null) {
            throw new RuntimeException("未获取到车道数据");
        }

        /**
         * {
         *     "data":[{
         *         "indexId":0,
         *         "vehNumber":"1",
         *         "sentryboxNo":"PTJS2",
         *         "parkKey":"ctpxbuv7",
         *         "parkScale":"0",
         *         "vehType":"0",
         *         "vehName":"入口车道1",
         *         "vehCtrlNo":"1",
         *         "vehVideoIp":"10.110.22.204",
         *         "vehCtrlIp":"10.110.22.204",
         *         "vehStatus":"1",
         *         "vehPointA":"",
         *         "vehPointB":"",
         *         "vehPointC":"",
         *         "vehPointD":"",
         *         "vehKey":""
         *     },
         *     {
         *         "indexId":1,
         *         "vehNumber":"2",
         *         "sentryboxNo":"PTJS2",
         *         "parkKey":"ctpxbuv7",
         *         "parkScale":"0",
         *         "vehType":"1",
         *         "vehName":"出口车道2",
         *         "vehCtrlNo":"2",
         *         "vehVideoIp":"10.110.22.170",
         *         "vehCtrlIp":"10.110.22.170",
         *         "vehStatus":"1",
         *         "vehPointA":"",
         *         "vehPointB":"",
         *         "vehPointC":"",
         *         "vehPointD":"",
         *         "vehKey":""
         *     }]
         * }
         *
         *
         *
         */

        List<JSONObject> jsonList = resultJsonArray.toJavaList(JSONObject.class);

        List<ProjectEntryExitLane> laneList = new ArrayList<>();
        ProjectEntryExitLane lane;
        for (JSONObject laneJson : jsonList) {
            lane = new ProjectEntryExitLane();
            lane.setLaneCode(laneJson.getString("vehNumber"));
            lane.setLaneName(laneJson.getString("vehName"));
            lane.setDirection("1".equals(laneJson.getString("vehType")) ? ParkingLaneConstant.LANE_IN : ParkingLaneConstant.LANE_OUT);
            lane.setMachineNo(laneJson.getString("vehCtrlNo"));
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

        VersionEnum version = VersionEnum.V1;

        JSONObject resultJsonObject = SfirmRemoteParkingServiceFactory.getInstance(version).getParkInfo(
                parkingInfo.getParkCode(),
                SfirmConstant.appId,
                SfirmConstant.appSecrets,
                null
        );


        if (resultJsonObject == null) {
            throw new RuntimeException("未获取到车场数据，请检查配置是否正确");
        }

//        {
//            "code":1,
//                "msg":"成功",
//                "rand":"5.215435987",
//                "sign":"9584359B37D51DC29B76F5A6D8679104",
//                "data":{
//            "indexId":0,
//                    "parkkey":"ctpxbuv7",
//                    "parkName":"FB2007068测试车场",
//                    "parkLatitude":"22.6107933258429",
//                    "parkLongitude":"114.013913498388",
//                    "parkAdd":"广东省深圳市南山区",
//                    "parkTel":"13888888888",
//                    "parkLinkman":"1",
//                    "parkFreeTime":"0",
//                    "parkFreeTimeout":"30",
//                    "chargesDesc":"0",
//                    "reserveStatus":"1",
//                    "regTime":"2020-07-17 11:50:48",
//                    "validTime":"2020-07-17 23:59:59",
//                    "spaceTotal":"100",
//                    "cityShortName":"粤B",
//                    "parkQRcodeUrl":"http://parkwx.szymzh.com/dwz?p=ygq8gcyn",
//                    "carTypeChargRules":[]
//        }
//        }

//        JSONObject dataJson = resultJsonObject.getJSONObject("data");
        String payImgUrl = resultJsonObject.getString("parkQRcodeUrl");
        return payImgUrl;
    }

    /**
     * 初始化支付类型
     *
     * @param parkId
     */
    @Override
    public List<ProjectParkBillingRule> getBillingRuleList(String parkId,Integer projectId) {
        List<ProjectParkBillingRule> parkBillingRuleList = new ArrayList<>();
        parkBillingRuleList.add(createBean("3651", "临时车A", parkId, ParkingRuleTypeEnum.TEMP, projectId));
        parkBillingRuleList.add(createBean("3650", "临时车B", parkId,ParkingRuleTypeEnum.TEMP, projectId));
        parkBillingRuleList.add(createBean("3649", "临时车C", parkId,ParkingRuleTypeEnum.TEMP, projectId));
        parkBillingRuleList.add(createBean("3648", "临时车D", parkId,ParkingRuleTypeEnum.TEMP, projectId));
        parkBillingRuleList.add(createBean("3652", "月租车A", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3653", "月租车B", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3654", "月租车C", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3655", "月租车D", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3661", "月租车E", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3662", "月租车F", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3663", "月租车G", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3664", "月租车H", parkId,ParkingRuleTypeEnum.MONTH, projectId));
        parkBillingRuleList.add(createBean("3656", "免费车", parkId,ParkingRuleTypeEnum.FREE, projectId));

        return parkBillingRuleList;
    }


    /**
     * 构造停车收费规则
     *
     * @param ruleCode
     * @param ruleName
     * @return
     */
    private ProjectParkBillingRule createBean(String ruleCode, String ruleName, String parkId,ParkingRuleTypeEnum ruleTypeEnum, Integer projectId) {
        ProjectParkBillingRule rule = new ProjectParkBillingRule();
        rule.setRuleCode(ruleCode);
        rule.setRuleName(ruleName);
        rule.setRuleName(ruleName);
        rule.setIsDisable(ruleTypeEnum==ParkingRuleTypeEnum.MONTH ? "1" : "0");
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
        return ParkingAPICompanyEnum.SFIRM.name();
    }
}
