package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceParkingPayConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPayDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.ParkingService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>停车场支付信息 统一消费者</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class EventDeviceParkingPayConsumer {
    @Resource
    private ParkingService parkingService;

//    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_EVENT_DEVICE_PARKING_PAY, errorHandler = "")
    @KafkaListener(topics = TopicConstant.SDI_EVENT_DEVICE_PARKING_PAY, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("车场支付信息 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            EventDeviceParkingPayDTO parkingPayDTO = JSONObject.parseObject(message, EventDeviceParkingPayDTO.class);

            switch (parkingPayDTO.getAction()) {
                case EventDeviceParkingPayConstant.ACTION_TEMP_CAR_PAY:
                    parkingService.pay(parkingPayDTO);
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
