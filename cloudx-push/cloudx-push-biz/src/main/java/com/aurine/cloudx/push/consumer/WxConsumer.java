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
 * <p>公众号 消费者</p>
 *
 * @ClassName: PublicAccountConsumer
 * @author: 邹宇
 * @date: 2021-8-24 15:19:18
 * @Copyright:
 */
@Component
@Slf4j
public class WxConsumer {

    @Autowired
    private SendMessageService pushNoticeService;


    @StreamListener(PushStreams.WX_OUT)
    public void handleGreetings(@Payload PushMessage message) throws Exception {
        log.info("Received greetings: {}", message);
        pushNoticeService.sendToWx(message);
    }
}
