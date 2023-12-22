package com.aurine.cloudx.edge.sync.biz.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.constant.KafkaConstant;
import com.aurine.cloudx.edge.sync.biz.service.biz.OpenApiPushService;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wrm
 * @Date: 2022/08/01 14:37
 * @Package: com.aurine.cloudx.edge.sync.biz.mq.kafka
 * @Version: 1.0
 * @Remarks: 接收kafka消息处理业务
 **/
@Component
@Slf4j
public class KafkaConsumer {

    @Resource
    private OpenApiPushService openApiPushService;

    /**
     * 配置类型消息订阅
     *
     * @param requestString
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_CONFIG)
    public void requestConfigListener(String requestString) {
        OpenApiEntity requestObj = JSONObject.parseObject(requestString, OpenApiEntity.class);

        // 判断是否为需要处理的消息请求
        if (!requestObj.getData().containsKey(Constants.REQUEST_STATUS_NAME)) {
            return;
        }

        // 判断是否为config类型数据，不是则不处理
        if (!OpenPushSubscribeCallbackTypeEnum.CONFIG.name.equals(requestObj.getServiceType())) {
            log.error("{}类型的数据被{}接收，存在异常订阅", requestObj.getServiceType(), KafkaConstant.TOPIC_CONFIG);
            return;
        }

        log.info("callback dealConfig, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
        openApiPushService.dealConfigRequest(requestObj);
    }

    /**
     * 推送需要关注顺序的通用消息，需要在一个partition有序执行
     * 包括，级联入云消息，操作类消息，命令类消息，其他类消息
     *
     * @param records
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_COMMON, containerFactory = "kafkaListenerContainerFactory")
    public void requestCommonListener(List<ConsumerRecord<String, String>> records) {
        try {
            log.info("开始消费{}数据，条数：{}", KafkaConstant.TOPIC_COMMON, records.size());
            for (ConsumerRecord<String, String> record : records) {
                OpenApiEntity requestObj = JSONObject.parseObject(record.value(), OpenApiEntity.class);

                // 判断消息类型具体请求，转发给不同业务处理
                switch (Objects.requireNonNull(OpenPushSubscribeCallbackTypeEnum.getByName(requestObj.getServiceType()))) {
                    case CASCADE:
                        log.info("callback dealCascade, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
                        openApiPushService.dealCascadeRequest(requestObj);
                        break;
                    case OPERATE:
                        log.info("callback dealOperate, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
                        openApiPushService.dealOperateRequest(requestObj);
                        break;
                    case COMMAND:
                        log.info("callback dealCommand, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
                        openApiPushService.dealCommandRequest(requestObj);
                        break;
                    case OTHER:
                        log.info("callback dealOther, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
                        openApiPushService.dealOtherRequest(requestObj);
                        break;
                    default:
                        break;
                }
            }
            log.info("完成消费{}数据", KafkaConstant.TOPIC_COMMON);
        } catch (Exception e) {
            log.error("数据同步消息消费异常", e);
        }
    }

    /**
     * 推送事件类消息
     *
     * @param records
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_EVENT, containerFactory = "kafkaListenerContainerFactory")
    public void requestEventListener(List<ConsumerRecord<String, String>> records) {
        log.info("开始消费{}数据，条数：{}", KafkaConstant.TOPIC_EVENT, records.size());
        for (ConsumerRecord<String, String> record : records) {
            OpenApiEntity requestObj = JSONObject.parseObject(record.value(), OpenApiEntity.class);

            // 判断是否为config类型数据，不是则不处理
            if (!OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(requestObj.getServiceType())) {
                log.error("{}类型的数据被{}接收，存在异常订阅", requestObj.getServiceType(), KafkaConstant.TOPIC_EVENT);
                return;
            }

            log.info("callback dealCascade, uuid为：{}，projectUUID为：{}，params：{} ", requestObj.getEntityId(), requestObj.getProjectUUID(), JSON.toJSONString(requestObj));
            openApiPushService.dealOperateRequest(requestObj);
        }
        log.info("完成消费{}数据", KafkaConstant.TOPIC_EVENT);
    }
}
