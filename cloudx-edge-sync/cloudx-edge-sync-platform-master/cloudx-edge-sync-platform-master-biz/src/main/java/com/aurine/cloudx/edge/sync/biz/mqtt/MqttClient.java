package com.aurine.cloudx.edge.sync.biz.mqtt;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.config.MqttParamConfig;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealRequestService;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealResponseService;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.enums.MessageTypeEnum;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.driver.dto.MqttCfgDto;
import com.aurine.driver.service.impl.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wrm
 * @Date: 2021/12/16 14:30
 * @Package: com.aurine.cloudx.open.mqtt
 * @Version: 1.0
 * @Remarks:
 **/
@Component
@Slf4j
public class MqttClient {

    private MqttService mqttService = new MqttService();

    @Resource
    private DealRequestService dealRequestService;

    @Resource
    private DealResponseService dealResponseService;

    @Resource
    private MqttParamConfig mqttParamConfig;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    public void initMqttClient() {

        //监听消息
        mqttService.setIMsgListener((topic, msg) -> {
            log.debug("Receive Mqtt Message, topic = {}", topic);
            String[] requestParams = topic.split("/");
            String requestType = requestParams[requestParams.length - 2];
            TaskInfoDto taskInfoDto = JSON.parseObject(msg, TaskInfoDto.class);
            try {
                if (MessageTypeEnum.REQUEST.name.equals(requestType)) {
                    dealRequestService.dealRequest(taskInfoDto);
                } else if (MessageTypeEnum.RESPONSE.name.equals(requestType)) {
                    dealResponseService.dealResponse(taskInfoDto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        //修改配置参数，连接
        MqttCfgDto mqttCfgDto = new MqttCfgDto();
        mqttCfgDto.setMqttBrokerIp(mqttParamConfig.getMqttBrokerIp());
        mqttCfgDto.setMqttBrokerPort(Short.parseShort(mqttParamConfig.getMqttBrokerPort()));
        mqttCfgDto.setUserName(mqttParamConfig.getUserName());
        mqttCfgDto.setPassword(mqttParamConfig.getPassword());
        mqttCfgDto.setMqttClientId(mqttParamConfig.getMqttClientId());
        mqttCfgDto.setQos(Short.parseShort("1"));
        //线程数
        mqttCfgDto.setThreadNum(100);
        //延迟
        mqttCfgDto.setDelay(0);

        //启动Mqtt服务
        //mqttService.start(mqttService, new String[]{});

        //启动
        mqttService.connectCfg(mqttService, mqttCfgDto);
        log.info("mqtt监听服务已启动");
    }

    /**
     * 添加 MQTT订阅消息列表 - 边缘侧主从端
     * 主题类型：
     * 从UUID/edge/platform/0000/VERSION/0/0/request/CMD
     * 从UUID/edge/platform/0000/VERSION/0/0/response/CMD
     */
    public void addSubscribeTopics(String... projectUUIDList) {
        String[] subscribeTopics = new String[projectUUIDList.length * 2];
        for (int i = 0; i < projectUUIDList.length; i += 1) {
            //订阅的主题,不同端代码不一致
            subscribeTopics[i * 2] = String.format(Constants.SUBSCRIBE_TOPIC_PREFIX, projectUUIDList[i], Constants.topicVersion, MessageTypeEnum.REQUEST.name);
            subscribeTopics[i * 2 + 1] = String.format(Constants.SUBSCRIBE_TOPIC_PREFIX, projectUUIDList[i], Constants.topicVersion, MessageTypeEnum.RESPONSE.name);
        }
        log.info("subscribeTopics: {}" + Arrays.toString(subscribeTopics));
        fixedThreadPool.execute(() -> {
            boolean success = false;
            AtomicInteger atomicInteger = new AtomicInteger();
            atomicInteger.set(0);

            while (!success && atomicInteger.get() < 3) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    atomicInteger.incrementAndGet();
                    mqttService.subscribe(subscribeTopics);
                    success = true;
                } catch (Exception e) {
                    log.error("mqtt连接失败 重试订阅：" + Arrays.toString(projectUUIDList), e);
                    e.printStackTrace();
                }
            }
            if (atomicInteger.get() >= 3) {
                log.info("mqtt重试订阅失败：" + Arrays.toString(projectUUIDList));
            }
        });
    }

    /**
     * 移除 MQTT订阅 - 边缘侧主从端
     * 主题类型：
     * 从UUID/edge/platform/0000/VERSION/0/0/request/CMD
     * 从UUID/edge/platform/0000/VERSION/0/0/response/CMD
     */
    public void removeSubscribeTopics(String projectUUID) {
        String[] subscribeTopics = new String[2];
        subscribeTopics[0] = String.format(Constants.SUBSCRIBE_TOPIC_PREFIX, projectUUID, Constants.topicVersion, MessageTypeEnum.REQUEST.name);
        subscribeTopics[1] = String.format(Constants.SUBSCRIBE_TOPIC_PREFIX, projectUUID, Constants.topicVersion, MessageTypeEnum.RESPONSE.name);
        mqttService.unSubscribe(subscribeTopics);
    }

    /**
     * 监听mqtt连接状态
     */
    public void mqttListener() {
        log.info("开始监听mqtt连接状态");
        mqttService.setIMqttConnectStatusListener((bool) -> {
            log.info("MQTT 连接状态:{}", bool);
            if (!bool) {
                //重试
                if (RedisUtil.hasKey(Constants.CONNECTION_STATUS)) {
                    int count = RedisUtil.get(Constants.CONNECTION_STATUS).hashCode();
                    if (mqttParamConfig.getRetryCount() > count) {
                        Integer val = count + 1;
                        RedisUtil.set(Constants.CONNECTION_STATUS, val, mqttParamConfig.getExpiration());
                    }
                } else {
                    RedisUtil.set(Constants.CONNECTION_STATUS, 1, mqttParamConfig.getExpiration());
                }
            } else {
                RedisUtil.del(Constants.CONNECTION_STATUS);
            }
        });
    }
}
