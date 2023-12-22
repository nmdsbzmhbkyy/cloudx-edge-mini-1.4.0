package com.aurine.cloud.code.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class CheckHealthKafka {


    @Autowired
    private KafkaTemplate kafkaTemplate;


    public void checkKafkaMsg(JSONObject jsonObject) {

        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(CheckHealthUtils.KAFKA_TOPIC_REQUEST, JSONObject.toJSONString(jsonObject));
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println(stringStringSendResult.getProducerRecord());
            }

            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.error("发送健康码校验结果失败");

            }


        });
    }



/*    @KafkaListener(topics = CheckHealthUtils.KAFKA_TOPIC_REQUEST)
    public  void checkKafkaComsumer(String msg){


        System.out.println("接收数据----------"+msg);
    }*/
}
