package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiMethodConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;


/**
 * 华为中台 请求消费者，用于请求的缓存，便于二次请求。
 *
 * @ClassName: HuaweiRequestConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03 15:00
 * @Copyright:
 */
//@Component
@Slf4j
public class HuaweiRequestConsumer {
    @Autowired
    private HuaweiDataConnector huaweiDataConnector;

    //topic模式的消费者
    @KafkaListener(groupId = HuaweiConstant.KAFKA_GROUP, topics = HuaweiConstant.KAFKA_TOPIC_REQUEST, errorHandler = "",id = HuaweiConstant.KAFKA_CLIEND_ID)
    public void readRequest(String message) {
        log.info("获取到请求数据，准备消费：{}", message);
        HuaweiRequestObject requestObject = null;
        try {
            requestObject = JSON.parseObject(message, HuaweiRequestObject.class);

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
    void doConnect(HuaweiRequestObject requestObject) {
        JSONObject respondJson = new JSONObject();
        if (HuaweiMethodConstant.POST.equalsIgnoreCase(requestObject.getMethod())) {
//            respondJson = huaweiDataConnector.post(requestObject.getUri(), requestObject.getVersion(), requestObject.getRequestJson());
            respondJson = huaweiDataConnector.post(requestObject);
        } else {
//            respondJson = huaweiDataConnector.get(requestObject.getUri(), requestObject.getVersion());
            respondJson = huaweiDataConnector.get(requestObject);
        }

        //TODO: 处理推送信息返回业务 王伟 @since 2020-08-03
        if(respondJson != null){
            log.info(respondJson.toJSONString());
        }

        //通知消费失败

        log.info("End Of Line");

    }
}
