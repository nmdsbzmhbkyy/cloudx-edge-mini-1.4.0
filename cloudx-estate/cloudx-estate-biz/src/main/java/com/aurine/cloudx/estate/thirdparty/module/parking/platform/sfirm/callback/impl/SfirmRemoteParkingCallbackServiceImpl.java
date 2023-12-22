package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.feign.RemoteFileService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceParkingPassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.callback.SfirmRemoteParkingCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.CarTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class SfirmRemoteParkingCallbackServiceImpl implements SfirmRemoteParkingCallbackService {
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


    /**
     * 回调添加车辆入场纪录
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void enterCar(JSONObject jsonObject) {

        /**
         * {
         *     "rand": "1596025934511",
         *     "data": {
         *         "key": "ctpxbuv7",
         *         "carNo": "闽DSB222",
         *         "orderNo": "20200729123209856-DSB222",
         *         "enterTime": "2020-07-29 12:32:09",
         *         "carType": "3651",
         *         "gateName": "入口车道1",
         *         "operatorName": "管理员",
         *         "reserveOrderNo": "",
         *         "imgUrl": ""
         *     },
         *     "method": "Car_EnterCar",
         *     "appid": "ymcef1d2949101da76",
         *     "sign": "E4726629DAF78C88444A02852837CDC7",
         *     "version": "1.0"
         * }
         *
         */
        //获取对应车厂信息
        String thirdCode = jsonObject.getString("key"); //车场第三方编号
        String orderNo = jsonObject.getString("orderNo"); //订单号
        String enterTime = jsonObject.getString("enterTime"); //入场时间
        LocalDateTime enterDateTime = DateUtil.parseLocalDateTime(enterTime, "yyyy-MM-dd HH:mm:ss"); //入场时间
        String gateName = jsonObject.getString("gateName"); //车道名称
        String operatorName = jsonObject.getString("operatorName"); //管理人
        String carNo = jsonObject.getString("carNo"); //车牌号


        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.SFIRM.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_ENTER);

        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setEnterDateTime(enterDateTime);
        eventDeviceParkingPassDTO.setGateName(gateName);
        eventDeviceParkingPassDTO.setOperatorName(operatorName);
        eventDeviceParkingPassDTO.setCarNo(carNo);


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
         *     "rand": "1596026117892",
         *     "data": {
         *         "key": "ctpxbuv7",
         *         "carNo": "辽AEB250",
         *         "orderNo": "20200729123159341-AEB250",
         *         "outTime": "2020-07-29 12:35:03",
         *         "carType": "3651",
         *         "gateName": "出口车道2",
         *         "operatorName": "管理员",
         *         "totalAmount": "0.01",
         *         "couponKey": "0",
         *         "imgUrl": "",
         *         "couponMoney": "0.00",
         *         "walletPayMoney": "0.00"
         *     },
         *     "method": "Car_OutCar",
         *     "appid": "ymcef1d2949101da76",
         *     "sign": "CA9E6100A0C0E43E3A8C60EAB2B7E662",
         *     "version": "1.0"
         * }
         */

        //获取对应车厂信息
        String thirdCode = jsonObject.getString("key"); //车场第三方编号
        String orderNo = jsonObject.getString("orderNo"); //订单号
        String outTime = jsonObject.getString("outTime"); //出场时间
        LocalDateTime outDateTime = DateUtil.parseLocalDateTime(outTime, "yyyy-MM-dd HH:mm:ss"); //出场时间
        String gateName = jsonObject.getString("gateName"); //车道名称
        String operatorName = jsonObject.getString("operatorName"); //管理人
        String carType = jsonObject.getString("carType"); //车辆类型，缴费规则

        EventDeviceParkingPassDTO eventDeviceParkingPassDTO = new EventDeviceParkingPassDTO();

        eventDeviceParkingPassDTO.setCompany(ParkingAPICompanyEnum.SFIRM.name());
        eventDeviceParkingPassDTO.setAction(EventDeviceParkingPassConstant.ACTION_CAR_OUTER);

        eventDeviceParkingPassDTO.setThirdCode(thirdCode);
        eventDeviceParkingPassDTO.setOrderNo(orderNo);
        eventDeviceParkingPassDTO.setOutDateTime(outDateTime);
        eventDeviceParkingPassDTO.setGateName(gateName);
        eventDeviceParkingPassDTO.setOperatorName(operatorName);


        //调用业务
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(thirdCode);

        if (parkingInfo != null) {
            kafkaTemplate.send(TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, JSONObject.toJSONString(eventDeviceParkingPassDTO));

            //如果是临时车，则调取缴费记录查询接口
            String ruleType = CarTypeEnum.getTypeByCode(carType);
            if (StringUtils.isNotEmpty(ruleType) && "3".equalsIgnoreCase(ruleType)) {
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
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


}
