package com.aurine.cloudx.estate.thirdparty.transport.mq.kafka;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.FujicaDispatchCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.callback.FujicaRemoteParkingCallbackService;
import com.aurine.cloudx.estate.thirdparty.transport.config.TransportConfig;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.project.entity.Event;
import com.aurine.project.entity.HeartBeat;
import com.aurine.project.entity.Message;
import com.aurine.project.entity.MessageDTO;
import com.aurine.project.entity.constant.ServiceProviderEnum;
import com.aurine.project.entity.constant.TransportTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneOffset;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-09
 * @Copyright:
 */
@Component
@Slf4j
public class KafkaConsumer {
    @Resource
    private TransportConfig transportConfig;
    @Resource
    private FujicaDispatchCallbackService fujicaDispatchCallbackService;
    @Resource
    private FujicaRemoteParkingCallbackService fujicaRemoteParkingCallbackService;




    /**
     * @param requestString
     */
    @KafkaListener(topics = TransportTopicConstant.CALL_BACK_TOPIC)
    public void requestListener(String requestString) {
        log.info("获取到项目侧回调数据:{}", requestString);

        //数据结构校验
        Message message = JSONObject.parseObject(requestString, Message.class);

        //返回数据存入
        RedisUtil.set(message.getCallBackMessageId(), JSONObject.toJSONString(message), transportConfig.timeout);

        //流程处理结束
        if (RedisUtil.hasKey(message.getMsgId())) {
            RedisUtil.delete(message.getMsgId());
        }
    }


    /**
     * 连接端心跳响应
     */
    @KafkaListener(topics = TransportTopicConstant.HEART_BEATS_TOPIC)
    public void heartBeatsListener(String requestString) {
        log.debug("获取到心跳信息:{}", requestString);

        HeartBeat heartBeat = JSONObject.parseObject(requestString, HeartBeat.class);
        String topic = heartBeat.getAppId()+"-"+heartBeat.getServiceProvider()+"-"+heartBeat.getProjectId();

        long time = heartBeat.getTime().toEpochSecond(ZoneOffset.of("+8"));
        RedisUtil.set(topic,time,150);
        //在线
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company", ParkingAPICompanyEnum.FUJICA.value);
        jsonObject.put("isOnline", '1');
        fujicaRemoteParkingCallbackService.isOnline(jsonObject);
    }

    /**
     * 异常主题
     * 用于监听异常的消息
     */
    @KafkaListener(topics = TransportTopicConstant.ERROR_TOPIC)
    public void errorListener(String requestString) {
        MessageDTO messageDTO = JSONObject.parseObject(requestString, MessageDTO.class);
        //TODO 暂时只做日志打印
        log.info("获取到异常信息:{}", messageDTO);

    }


    /**
     * 事件主题
     * 用于监听主动返回的事件
     */
    @KafkaListener(topics = TransportTopicConstant.EVENT_TOPIC)
    public void eventListener(String requestString) {
        log.info("获取到事件信息:{}", requestString);

        Event event = JSONObject.parseObject(requestString, Event.class);

        //根据事件类型分发回调方法
        ServiceProviderEnum serviceProviderEnum = ServiceProviderEnum.getByCode(event.getServiceProvider());
        if (serviceProviderEnum == null) {
//            log.info("");
        } else if (serviceProviderEnum == ServiceProviderEnum.PARKING_FUJICA) {//富士车场
            fujicaDispatchCallbackService.dispatch(event);
        }


    }

}
