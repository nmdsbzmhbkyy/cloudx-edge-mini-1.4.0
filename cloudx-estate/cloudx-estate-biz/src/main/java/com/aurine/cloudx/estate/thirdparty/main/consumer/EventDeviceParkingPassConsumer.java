package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceParkingPassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.ParkingService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.ALERT_TYPE;
import static com.aurine.cloudx.estate.constant.decidedTypeConstant.CAR_TYPE;

/**
 * <p>停车场出入记录 统一消费者</p>
 *
 * @ClassName: EventDeviceParkingPassConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class EventDeviceParkingPassConsumer {
    @Resource
    private ParkingService parkingService;
    @Resource
    ProjectParkingInfoService projectParkingInfoService;
    @Resource
    private EventFactory eventFactory;

    //    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, errorHandler = "")
    @KafkaListener(topics = TopicConstant.SDI_EVENT_DEVICE_PARKING_PASS, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("车场通行记录 统一接口 ：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            EventDeviceParkingPassDTO parkingPassDTO = JSONObject.parseObject(message, EventDeviceParkingPassDTO.class);
            /**
             * 监听车行记录    订阅
             */
//            ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(parkingPassDTO.getThirdCode());
//            if (parkingInfo != null && !EventDeviceParkingPassConstant.ACTION_CAR_ENTER_IMG.equals(parkingPassDTO.getAction())) {
//                JSONObject messageObject = JSONObject.parseObject(message);
//                messageObject.put("parkId", parkingInfo.getParkId());
//                messageObject.put("parkName", parkingInfo.getParkName());
//                EventSubscribeService eventSubscribeService = eventFactory.GetProduct(CAR_TYPE);
//                eventSubscribeService.send(messageObject, parkingInfo.getProjectId(), CAR_TYPE);
//            }

            switch (parkingPassDTO.getAction()) {
                case EventDeviceParkingPassConstant.ACTION_CAR_ENTER:
                    parkingService.enterCar(parkingPassDTO);
                    break;
                case EventDeviceParkingPassConstant.ACTION_CAR_OUTER:
                    parkingService.outerCar(parkingPassDTO);
                    break;
                case EventDeviceParkingPassConstant.ACTION_CAR_ENTER_IMG:
                    parkingService.enterImg(parkingPassDTO);
                    break;
                case EventDeviceParkingPassConstant.ACTION_CAR_OUTER_IMG:
                    parkingService.outerImg(parkingPassDTO);
                    break;
                default:
                    //抛弃处理
                    break;
            }

        } catch (Exception e) {
            //JSON解析失败，抛弃处理
        }
    }
}
