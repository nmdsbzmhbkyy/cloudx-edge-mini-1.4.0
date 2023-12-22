package com.aurine.cloudx.estate.openapi.consumer;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 处理入云的消费者
 *
 * @author:zouyu
 * @data:2022/5/24 5:09 下午
 */
@Component
@Slf4j
public class IntoCloudConsumer {

    @KafkaListener(topics = TopicConstant.EDGE_SYNC_EDGE_PLATFORM)
    public void requestListener(String messageStr) {
        //TODO 暂时只做日志打印
        log.info("边缘网关推送消息至Open服务异常的数据:{}", messageStr);
    }
}
