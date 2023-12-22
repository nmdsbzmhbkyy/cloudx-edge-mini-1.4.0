package com.aurine.cloudx.push.handler;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.push.entity.PushMessage;
import com.aurine.cloudx.push.service.SendMessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>推送消息处理器 - Kafka消费者</p>
 *
 * @ClassName: PushNoticeConsumerHandler
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/28 10:03
 * @Copyright:
 */
public class PushConsumerHandler {
    @Autowired
    private SendMessageService pushNoticeService;


    @KafkaListener(topics = "PUSH_NOTICE", errorHandler = "")
    void receiveNoticeConsumer(ConsumerRecord<String, String> consumerRecord) throws Exception {
        PushMessage message = JSONObject.parseObject(consumerRecord.value()).toJavaObject(PushMessage.class);
        this.dispath(message);
    }

    @Async
    void dispath(PushMessage message) throws Exception {
        // 根据类型发送给对应目标
        switch (message.getPushType()) {
            case SMS:
                pushNoticeService.sendToSMS(message);
                break;
            case SMSS:
                pushNoticeService.sendToSMSs(message);
                break;
            case EMAIL:
                pushNoticeService.sendToEmail(message);
                break;
            case EMAILS:
                pushNoticeService.sendToEmails(message);
                break;
            case APP:
                pushNoticeService.sendToApp(message);
                break;
            case APPS:
                pushNoticeService.sendToApps(message);
                break;
            default:
        }
    }
}
