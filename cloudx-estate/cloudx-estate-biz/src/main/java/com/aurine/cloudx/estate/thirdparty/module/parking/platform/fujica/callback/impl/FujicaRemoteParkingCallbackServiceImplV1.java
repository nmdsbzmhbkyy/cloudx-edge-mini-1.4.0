package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.feign.RemoteFileService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceParkingPassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.FujicaRemoteParkingCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.entity.constant.CardTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.CarTypeEnum;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>赛菲姆车场对接，回调接口实现</p>
 *
 * @ClassName: SfirmRemoteParkingCallbackServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 14:15
 * @Copyright:
 */
@Service
@Slf4j
public class FujicaRemoteParkingCallbackServiceImplV1 implements FujicaRemoteParkingCallbackService {
    //    @Autowired
//    ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    ProjectParkingInfoService projectParkingInfoService;
    //    @Autowired
//    ProjectCarInfoService projectCarInfoService;
//    @Autowired
//    ProjectParkBillingRuleService projectParkBillingRuleService;
//    @Autowired
//    ProjectPersonInfoService projectPersonInfoService;
//    @Autowired
//    ProjectParkEntranceHisService projectParkEntranceHisService;
    @Resource
    private KafkaTemplate kafkaTemplate;

    @Resource
    private RemoteFileService remoteFileService;
    @Resource
    private ImgConvertUtil imgConvertUtil;


    /**
     * 回调添加车辆入场纪录
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void enterCar(JSONObject jsonObject) {

        /**
         *{
         *         "InOperatorId": "69CBCBD5-E33E-41F9-BAAC-FC8571BCEAE7",
         *         "TerminalName": "地面岗亭一",
         *         "InAutoPlate": "粤BD12345",
         *         "TcmName": "月租卡A",
         *         "RegPlate": "粤BD12345",
         *         "InFlag": 0,
         *         "InLaneId": "52F51E6D-815C-4B23-AD68-75F6AA70832F",
         *         "NoSenseMark": "[]",
         *         "TokenType": 0,
         *         "ParkId": "DC1FF199-C295-4DF2-8D89-57B608758114",
         *         "VehicleBand": "",
         *         "InType": 0,
         *         "TerminalId": "8E373F29-2FAB-496D-88DA-18DB93A1880F",
         *         "ID": 20021,
         *         "ParkingId": "6a50d74e-f59b-43df-95c4-80aa05987971",
         *         "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *         "InLaneName": "出口车道一1",
         *         "InOperatorName": "超级管理员",
         *         "Rid": "bf8e0404-fe9b-4db2-854d-08e600db187f",
         *         "StaffName": "李月",
         *         "Province": "广东",
         *         "InPictureStaff": "",
         *         "TokenNo": "粤BD12345",
         *         "PayMark": 0,
         *         "InPicture": "http://10.110.22.208:60001/20210521/16/20210521160700_in_v_1_粤BD12345.jpg",
         *         "LastChargeTime": "2021-05-21T16:07:00",
         *         "VehicleColor": " 蓝",
         *         "InTime": "2021-05-21T16:07:00",
         *         "State": 0,
         *         "TcmId": "C97611CE-C6EA-4582-9680-55934B79F9F7",
         *         "InPicture2": "http://10.110.22.208:60001/20210521/16/20210521160700_in_v_2_粤BD12345.jpg",
         *         "InRemark": "",
         *         "LotFullRemark": "",
         *         "PlateColor": "绿色",
         *         "GroupLotState": 0,
         *         "VehicleCategory": "小型车",
         *         "StaffNo": "SI19352",
         *         "ReservationNo": ""
         * }
         *
         */
        //获取对应车厂信息
        String thirdCode = jsonObject.getString("Gid"); //车场第三方编号
//        String orderNo = jsonObject.getString("orderNo"); //订单号
        String enterTime = jsonObject.getString("InTime"); //入场时间
        LocalDateTime enterDateTime = DateUtil.parseLocalDateTime(enterTime.replaceAll("T"," "), "yyyy-MM-dd HH:mm:ss"); //入场时间
        String gateName = jsonObject.getString("InLaneName"); //车道名称
        String operatorName = jsonObject.getString("InOperatorName"); //管理人
        String carNo = jsonObject.getString("TokenNo"); //车牌号

        String orderNo = carNo + enterTime; //订单号

        //检查是否已存在记录(时间+车牌)


        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.FUJICA.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_ENTER);


        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setEnterDateTime(enterDateTime);
        eventDeviceParkingPassDTO.setGateName(gateName);
        eventDeviceParkingPassDTO.setOperatorName(operatorName);
        eventDeviceParkingPassDTO.setCarNo(carNo);

        //转存图片
        try {
            eventDeviceParkingPassDTO.setImgUrl(imgConvertUtil.base64ToMinio(jsonObject.getString("InPicture")));
        } catch (IOException e) {
            log.error("[富士车场] BASE64数据转存图片失败");
            e.printStackTrace();
        }


        kafkaTemplate.send(TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, JSONObject.toJSONString(eventDeviceParkingPassDTO));
    }

    /**
     * 回调添加车辆出场纪录
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void outerCar(JSONObject jsonObject) {
        /***
         * {
         "ID": 10029,
         "ParkingId": "a7c0146a-5ef9-4fb1-80b3-795c6290c003",
         "TokenNo": "黑L04444",
         "TokenType": 0,
         "ParkId": "DC1FF199-C295-4DF2-8D89-57B608758114",
         "TcmId": "C97611CE-C6EA-4582-9680-55934B79F9F7",
         "TcmName": "月租卡A",
         "StaffNo": "SI19363",
         "StaffName": "杨紫",
         "RegPlate": "黑L04444",
         "InAutoPlate": "黑L04444",
         "InLaneName": "出口车道一1",
         "InTime": "2021-05-24T10:24:48",
         "InPicture": "http://10.110.22.208:60001/20210524/10/20210524102448_in_v_1_黑L04444.jpg",
         "InPicture2": "http://10.110.22.208:60001/20210524/10/20210524102448_in_v_2_黑L04444.jpg",
         "InPictureStaff": "",
         "InOperatorId": "69CBCBD5-E33E-41F9-BAAC-FC8571BCEAE7",
         "InOperatorName": "超级管理员",
         "InType": 0,
         "InFlag": 0,
         "InLaneId": "52F51E6D-815C-4B23-AD68-75F6AA70832F",
         "LotFullRemark": "",
         "GroupLotState": 0,
         "ReservationNo": "",
         "InTerminalId": "8E373F29-2FAB-496D-88DA-18DB93A1880F",
         "InTerminalName": "地面岗亭一",
         "InRemark": "",
         "VehicleColor": " 蓝",
         "VehicleCategory": "小型车",
         "VehicleBand": "",
         "PlateColor": "蓝色",
         "OutAutoPlate": "黑L04444",
         "OutLaneName": "出口车道一1",
         "OutTime": "2021-05-24T10:28:44",
         "OutPicture": "http://10.110.22.208:60001/20210524/10/20210524102844_out_v_1_黑L04444.jpg",
         "OutPicture2": "http://10.110.22.208:60001/20210524/10/20210524102844_out_v_2_黑L04444.jpg",
         "OutPictureStaff": "",
         "OutOperatorId": "69CBCBD5-E33E-41F9-BAAC-FC8571BCEAE7",
         "OutOperatorName": "超级管理员",
         "OutType": 0,
         "OutFlag": 0,
         "OutLaneId": "52F51E6D-815C-4B23-AD68-75F6AA70832F",
         "OutRemark": "",
         "StayLasts": "0时3分56秒",
         "TerminalId": "8E373F29-2FAB-496D-88DA-18DB93A1880F",
         "TerminalName": "地面岗亭一",
         "TranAmount": 0.0000,
         "AccountPayAmount": 0.0000,
         "CashAmount": 0.0000,
         "FreeAmount": 0.0000,
         "DeductedAmount": 0.0000,
         "DeductedHours": 0.0,
         "DeductedHoursAmount": 0.0000,
         "Province": "黑龙江",
         "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         "Rid": "81ab8679-1f48-4e0f-a288-a08f0caa7f49"
         * }
         */

        //获取对应车厂信息
        String thirdCode = jsonObject.getString("Gid"); //车场第三方编号
//        String orderNo = jsonObject.getString("orderNo"); //订单号
        String outTime = jsonObject.getString("OutTime"); //出场时间
        LocalDateTime outDateTime = DateUtil.parseLocalDateTime(outTime.replaceAll("T"," "), "yyyy-MM-dd HH:mm:ss"); //出场时间
        String gateName = jsonObject.getString("OutLaneName"); //车道名称
        String operatorName = jsonObject.getString("OutOperatorName"); //管理人
        String carType = jsonObject.getString("TcmId"); //车辆类型，缴费规则

        String enterTime = jsonObject.getString("InTime"); //入场时间
        String carNo = jsonObject.getString("TokenNo"); //车牌号
        String orderNo = carNo + enterTime; //订单号

        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.FUJICA.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_OUTER);

        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setOutDateTime(outDateTime);
        eventDeviceParkingPassDTO.setGateName(gateName);
        eventDeviceParkingPassDTO.setOperatorName(operatorName);

        //转存图片
        try {
            eventDeviceParkingPassDTO.setImgUrl(imgConvertUtil.base64ToMinio(jsonObject.getString("OutPicture")));
        } catch (IOException e) {
            log.error("[富士车场] BASE64数据转存图片失败");
            e.printStackTrace();
        }

        //调用业务
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(thirdCode);

        if (parkingInfo != null) {
            kafkaTemplate.send(TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, JSONObject.toJSONString(eventDeviceParkingPassDTO));

            //如果是临时车，则调取缴费记录查询接口
            //TODO:等待缴费记录接口确认
            if (CardTypeEnum.getCloudCodeByFujica(carType) == ParkingRuleTypeEnum.TEMP) {
                ParkingFactoryProducer.getFactory(parkingInfo.getParkId()).getParkingService().getTmpPayRecords(parkingInfo.getParkId(), orderNo);
            }

        } else {
            //记录异常,未知车场
        }
    }

    /**
     * 回调添加车辆入场图片
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void enterImg(JSONObject jsonObject) {
        /**
         * {
         *     "rand": "1596025939166",
         *     "data": {
         *         "key": "ctpxbuv7",
         *         "orderNo": "20200729123209856-DSB222",
         *         "carNo": "闽DSB222",
         *         "imgUrl": "http://sfm-image.oss-cn-shenzhen.aliyuncs.com/4de3d5e2-29c4-453f-8e94-9074bcb0da1320200729123209c?Expires=2542681933&OSSAccessKeyId=LTAImO0B1h7uYZZY&Signature=taCqwJl9LTK%2B8Jd09iWRGJJIO%2Fk%3D"
         *     },
         *     "method": "Car_EnterImg",
         *     "appid": "ymcef1d2949101da76",
         *     "sign": "A91AC66E2C4E43E3CF287AE9149C217D",
         *     "version": "1.0"
         * }
         */

        //获取对应车厂信息
        String thirdCode = jsonObject.getString("key"); //车场第三方编号
        String orderNo = jsonObject.getString("orderNo"); //订单号
        String imgUrl = jsonObject.getString("imgUrl"); //图片地址

        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.SFIRM.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_ENTER_IMG);

        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setImgUrl(imgUrl);

        kafkaTemplate.send(TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, JSONObject.toJSONString(eventDeviceParkingPassDTO));

    }

    /**
     * 回调添加车辆出场图片
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void outerImg(JSONObject jsonObject) {
        //获取对应车厂信息
        String thirdCode = jsonObject.getString("key"); //车场第三方编号
        String orderNo = jsonObject.getString("orderNo"); //订单号
        String imgUrl = jsonObject.getString("imgUrl"); //图片地址

        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.SFIRM.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_OUTER_IMG);

        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setImgUrl(imgUrl);


        kafkaTemplate.send(TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, JSONObject.toJSONString(eventDeviceParkingPassDTO));
    }

    /**
     * 设置车场在线状态
     *
     * @param jsonObject
     */
    @Override
    public void isOnline(JSONObject jsonObject) {
        String company = jsonObject.getString("company");
        char isOnline = (char) jsonObject.get("isOnline");

        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();
        eventDeviceParkingPassDTO.setCompany(company);
        eventDeviceParkingPassDTO.setIsOnline(isOnline);
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_STATUS);

        kafkaTemplate.send(TopicConstant.SDI_EVENT_PARKING_STATUS,JSONObject.toJSONString(eventDeviceParkingPassDTO));

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


}
