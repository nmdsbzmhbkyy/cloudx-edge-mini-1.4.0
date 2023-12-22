package com.aurine.cloudx.estate.thirdparty.main.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.service.ProjectDeviceEventCallbackService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventWarningErrorConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventIotDeviceCallbackDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.EventService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.ALERT_TYPE;

/**
 * <p>异常事件通知 统一消费者</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class EventWarningErrorConsumer {
    @Resource
    private EventService eventService;
    @Resource
    private ProjectDeviceEventCallbackService projectDeviceEventCallbackService;
    @Resource
    private EventFactory eventFactory;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    //    @KafkaListener(groupId = "THIRD_GROUP", topics = TopicConstant.SDI_EVENT_WARNING_ERROR, errorHandler = "")
    @KafkaListener(topics = TopicConstant.SDI_EVENT_WARNING_ERROR, errorHandler = "")
    public void readActiveQueue(String message) {
        log.info("告警异常事件通知 统一接口 接口接收到数据：{}", message);
        dispatch(message);
    }

    @KafkaListener(topics = TopicConstant.IOT_DEVICE_EVENT, errorHandler = "")
    public void iotDeviceEventListener(String message) {
        log.info("物联网设备事件：{}", message);
        projectDeviceEventCallbackService.saveBatch(JSONObject.parseObject(message, EventIotDeviceCallbackDTO.class).getIotEventCallbackSet());
    }

    @Async
    void dispatch(String message) {
        TenantContextHolder.setTenantId(1);
        EventWarningErrorDTO warningErrorDTO = JSONObject.parseObject(message, EventWarningErrorDTO.class);
        /**
         * 监听警情记录    订阅
         */
//        String thirdPartyCode = warningErrorDTO.getThirdPartyCode();
//        String deviceSn = warningErrorDTO.getDeviceSn();
//        ProjectDeviceInfoProxyVo deviceInfo;
//        if (StringUtils.isNotEmpty(thirdPartyCode)) {
//            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
//        } else {
//            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
//        }
//        if (deviceInfo != null) {
//            JSONObject messageObject = JSONObject.parseObject(message);
//            messageObject.put("deviceId", deviceInfo.getDeviceId());
//            messageObject.put("deviceName", deviceInfo.getDeviceName());
//            EventSubscribeService eventSubscribeService = eventFactory.GetProduct(ALERT_TYPE);
//            eventSubscribeService.send(messageObject, deviceInfo.getProjectId(), ALERT_TYPE);
//
//        }


        try {
            switch (warningErrorDTO.getAction()) {
                case EventWarningErrorConstant.ACTION_ERROR:
                    eventService.warning(warningErrorDTO);
                    break;
                case EventWarningErrorConstant.ACTION_CHANNEL_ALARM:
                    eventService.channelAlarm(warningErrorDTO);
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
