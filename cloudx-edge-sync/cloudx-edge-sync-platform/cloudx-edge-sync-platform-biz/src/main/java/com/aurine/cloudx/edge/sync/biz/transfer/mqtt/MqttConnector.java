package com.aurine.cloudx.edge.sync.biz.transfer.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.enums.MessageTypeEnum;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;

import com.aurine.driver.service.impl.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Resource
    private ProjectRelationService projectRelationService;

    public void publishRequestMessage(TaskInfoDto taskInfoDto) {
        log.debug("[{}] 开始发送MQTT请求类型消息", taskInfoDto.getMsgId());
        String topic = getPublishTopic(taskInfoDto, MessageTypeEnum.REQUEST.name);
        log.debug("PUBLISH MQTT REQUEST TOPIC : {}", topic);
        log.debug("[{}] PUBLISH MQTT REQUEST, TOPIC = {}， MESSAGE : {}", taskInfoDto.getMsgId(), topic, JSON.toJSONString(taskInfoDto));
        mqttService.publish(topic, JSONObject.parseObject(JSON.toJSONString(taskInfoDto)));
        log.debug("[{}] 完成发送MQTT请求类型消息", taskInfoDto.getMsgId());
    }

    public void publishResponseMessage(TaskInfoDto taskInfoDto) {
        taskInfoDto.setData(null);
        log.debug("[{}] 开始发送MQTT响应类型消息", taskInfoDto.getMsgId());
        String topic = getPublishTopic(taskInfoDto, MessageTypeEnum.RESPONSE.name);
        log.info("PUBLISH MQTT RESPONSE TOPIC : {}", topic);
        log.debug("[{}] PUBLISH MQTT RESPONSE, TOPIC = {}， MESSAGE : {}", taskInfoDto.getMsgId(), topic, JSON.toJSONString(taskInfoDto));
        mqttService.publish(topic, JSONObject.parseObject(JSON.toJSONString(taskInfoDto)));
        log.debug("[{}] 完成发送MQTT响应类型消息", taskInfoDto.getMsgId());
    }

    /**
     * 获取请求topic,不同端代码不一致
     *
     *
     * @param requestObj
     * @param type
     * @return
     */
    private String getPublishTopic(TaskInfoDto requestObj, String type) {
        ProjectRelation byProjectCode = projectRelationService.getByProjectRelation(requestObj.getProjectUUID(), requestObj.getProjectCode());
        return String.format(Constants.PUBLISH_TOPIC_PREFIX, byProjectCode.getSn(), byProjectCode.getProjectCode(), Constants.topicVersion, type, requestObj.getServiceType());
    }
}
