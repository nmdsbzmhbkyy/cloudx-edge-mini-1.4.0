package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * 冠林中台 请求消费者，用于请求的缓存，便于二次请求。
 *
 * @ClassName: AurineRequestConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03 15:00
 * @Copyright:
 */
@Component
@Slf4j
public class AurineRequestConsumer {
    @Autowired
    private AurineDataConnector aurineDataConnector;

    //topic模式的消费者
    @KafkaListener(groupId = AurineConstant.KAFKA_GROUP, topics = AurineConstant.KAFKA_TOPIC_REQUEST, errorHandler = "", id = AurineConstant.KAFKA_CLIEND_ID)
    public void readRequest(String message) {
        log.info("获取到请求数据，准备消费：{}", message);

        AurineRequestObject requestObject = null;
        try {
            requestObject = JSON.parseObject(message, AurineRequestObject.class);

        } catch (Exception e) {
            //非法数据
            e.printStackTrace();
            log.error("非法数据：{}", message);
        }

        if (requestObject != null) {
            doConnect(requestObject);
        }
    }

    //    @Async
    void doConnect(AurineRequestObject requestObject) {

        //检查是否重复消费
        if (RedisUtil.hasKey(requestObject.getMessageId())) {
    log.info("信息 {} 被重复消费，已忽略",requestObject.getMessageId());
        } else {
            JSONObject respondJson = new JSONObject();
//            if (AurineMethodConstant.POST.equalsIgnoreCase(requestObject.getMethod())) {
//                respondJson = aurineDataConnector.post(requestObject);
//            } else {
//                respondJson = aurineDataConnector.get(requestObject);
//            }
            respondJson = aurineDataConnector.send(requestObject);

            //TODO: 处理推送信息返回业务 王伟 @since 2020-08-03
            if (respondJson != null) {
                log.info(respondJson.toJSONString());
            }
        }

        //通知消费失败
        log.info("End Of Line");

    }
}
