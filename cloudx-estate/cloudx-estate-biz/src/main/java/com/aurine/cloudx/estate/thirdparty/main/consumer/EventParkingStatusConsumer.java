package com.aurine.cloudx.estate.thirdparty.main.consumer;


import com.alibaba.fastjson.JSONObject;
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

@Component
@Slf4j
public class EventParkingStatusConsumer {
    @Resource
    private ParkingService parkingService;

    @KafkaListener(topics = TopicConstant.SDI_EVENT_PARKING_STATUS, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("车场在线状态 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            EventDeviceParkingPassDTO parkingPassDTO = JSONObject.parseObject(message, EventDeviceParkingPassDTO.class);
            switch (parkingPassDTO.getAction()) {
                case EventDeviceParkingPassConstant.ACTION_CAR_STATUS:
                    parkingService.parkingStatus(parkingPassDTO);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            log.info("JSON数据解析异常:{}",message);
        }

    }
}
