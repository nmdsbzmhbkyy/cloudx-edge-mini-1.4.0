package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.AurineConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceNoticeConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.DeviceService;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>设备事件通知 统一消费者</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class EventDeviceNoticeConsumer {
    @Resource
    private DeviceService deviceService;


//    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_EVENT_DEVICE_NOTICE, errorHandler = "")
    @KafkaListener(topics = TopicConstant.SDI_EVENT_DEVICE_NOTICE, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("设备事件通知 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        try {
            EventDeviceNoticeDTO noticeDTO = JSONObject.parseObject(message, EventDeviceNoticeDTO.class);

            switch (noticeDTO.getAction()) {
                case EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE:
                    deviceService.deviceStatusUpdate(noticeDTO);
                    break;
                case EventDeviceNoticeConstant.ACTION_DEVICE_CALL_EVENT:
                    deviceService.saveCallEvent(noticeDTO);
                    break;
                default:
                    //抛弃处理
                    break;
            }

        } catch (Exception e) {
            //JSON解析失败，抛弃处理
            e.printStackTrace();
        }
    }
}
