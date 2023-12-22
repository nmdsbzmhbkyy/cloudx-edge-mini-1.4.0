package com.aurine.cloudx.edge.sync.biz.transfer.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.enums.MessageTypeEnum;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.driver.service.impl.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: wrm
 * @Date: 2022/01/20 11:33
 * @Package: com.aurine.cloudx.edge.sync.biz.transfer.mqtt
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Component
public class MqttConnector {

    private MqttService mqttService = new MqttService();

    public void publishRequestMessage(TaskInfoDto taskInfoDto) {
        String topic = getPublishTopic(taskInfoDto, MessageTypeEnum.REQUEST.name);
        log.info("PUBLISH MQTT REQUEST TOPIC : {}", topic);
        log.info("PUBLISH MQTT REQUEST : {}", JSON.toJSONString(taskInfoDto));
        mqttService.publish(topic, JSONObject.parseObject(JSON.toJSONString(taskInfoDto)));
    }

    public void publishResponseMessage(TaskInfoDto taskInfoDto) {
        String topic = getPublishTopic(taskInfoDto, MessageTypeEnum.RESPONSE.name);
        log.info("PUBLISH MQTT RESPONSE TOPIC : {}", topic);
        log.info("PUBLISH MQTT RESPONSE : {}", JSON.toJSONString(taskInfoDto));
        mqttService.publish(topic, JSONObject.parseObject(JSON.toJSONString(taskInfoDto)));
    }

    /**
     * 获取请求topic,不同端代码不一致
     *
     * @param requestObj
     * @param type
     * @return
     */
    private String getPublishTopic(TaskInfoDto requestObj, String type) {
        return String.format(Constants.PUBLISH_TOPIC_PREFIX, requestObj.getProjectUUID(), Constants.topicVersion, type, requestObj.getServiceType());
    }
}
