package com.aurine.cloudx.push.consumer;

import com.aurine.cloudx.push.entity.PushMessage;
import com.aurine.cloudx.push.service.SendMessageService;
import com.aurine.cloudx.push.stream.PushStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * <p>sms 消费者</p>
 *
 * @ClassName: EmailConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/5 9:35
 * @Copyright:
 */
@Component
@Slf4j
public class SMSsConsumer {
    @Autowired
    private SendMessageService pushNoticeService;


    @StreamListener(PushStreams.SMSS_OUT)
    public void handleGreetings(@Payload PushMessage message) throws Exception {
        log.info("Received greetings: {}", message);
        pushNoticeService.sendToSMSs(message);
    }
}
