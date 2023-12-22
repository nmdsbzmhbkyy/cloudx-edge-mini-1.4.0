package com.aurine.cloudx.estate.openapi.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.openapi.constant.KafkaSyncConstant;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: wrm
 * @Date: 2021/12/10 16:30
 * @Package: com.example.aoptest.cloudopenapi
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Service
public class OpenApiMessageService {

    @Resource
    @Lazy
    private ProjectInfoService projectInfoService;

    private static Map projectIdRelMap = new HashMap<String, String>();

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    private ExecutorService pool = Executors.newFixedThreadPool(5);

    @SneakyThrows
    public void sendOpenApiMessage(OpenApiEntity openApiEntity) {
        if (OpenApiServiceNameEnum.CASCADE_APPLY.name.equals(openApiEntity.getServiceName())) {
            // 级联申请操作需要转换projectUUID
            // projectUUID 改为申请的projectUUID
            ObjectNode objectNode = objectMapper.readValue(objectMapper.writeValueAsString(openApiEntity.getData()), ObjectNode.class);
            JsonNode projectId = objectNode.findPath("projectId");
            openApiEntity.setProjectUUID(projectInfoService.getProjectUUID(projectId.asInt()));

        } else if (OpenPushSubscribeCallbackTypeEnum.OTHER.name.equals(openApiEntity.getServiceType())) {
            String projectUUID = getProjectUUID();
            if (projectUUID == null) {
                log.info("找不到项目UUID");
                return;
            }
            openApiEntity.setProjectUUID(projectUUID);
        } else {
            openApiEntity.setProjectUUID(projectInfoService.getProjectUUID(ProjectContextHolder.getProjectId()));

//            ObjectNode objectNode = objectMapper.readValue(objectMapper.writeValueAsString(openApiEntity.getData()), ObjectNode.class);
//            JsonNode projectId = objectNode.findPath("projectId");
//            if (!projectId.isMissingNode()) {
//                openApiEntity.setProjectUUID(projectInfoService.getProjectUUID(projectId.asInt()));
//            } else {
//                openApiEntity.setProjectUUID(projectInfoService.getProjectUUID(ProjectContextHolder.getProjectId()));
//            }
            /*String projectUUID = getProjectUUID();
            if (projectUUID == null) {
                log.info("找不到项目UUID");
                return;
            }
            openApiEntity.setProjectUUID(projectUUID);*/
        }
        openApiEntity.setTenantId(1);
        // 发送kafka消息
        sendMessageByServiceType(openApiEntity);
    }

    @SneakyThrows
    public void sendMessageByServiceType(OpenApiEntity openApiEntity) {
        log.info("边缘网关推送消息至Open服务:{}", JSONObject.toJSONString(openApiEntity));
        try {
            switch (OpenPushSubscribeCallbackTypeEnum.getByName(openApiEntity.getServiceType())) {
                case CONFIG:
                    log.info("send config msg = {} ", KafkaSyncConstant.TOPIC_PUSH_CONFIG);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_CONFIG, openApiEntity);
                    break;
                case CASCADE:
                    log.info("send cascade msg = {} ", KafkaSyncConstant.TOPIC_PUSH_CASCADE);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_CASCADE, openApiEntity);
                    break;
                case OPERATE:
                    log.info("send operate msg = {} ", KafkaSyncConstant.TOPIC_PUSH_OPERATE);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_OPERATE, openApiEntity);
                    break;
                case EVENT:
                    log.info("send event msg = {} ", KafkaSyncConstant.TOPIC_PUSH_EVENT);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_EVENT, openApiEntity);
                    break;
                case COMMAND:
                    log.info("send command msg = {} ", KafkaSyncConstant.TOPIC_PUSH_COMMAND);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_COMMAND, openApiEntity);
                    break;
                case OTHER:
                    log.info("send other msg = {} ", KafkaSyncConstant.TOPIC_PUSH_OTHER);
                    KafkaProducer.sendMessage(KafkaSyncConstant.TOPIC_PUSH_OTHER, openApiEntity);
                    break;
                default:
                    break;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.info("边缘网关推送消息至Open服务出现异常:{}", e.getMessage());
            KafkaProducer.sendMessage(TopicConstant.EDGE_SYNC_EDGE_PLATFORM, openApiEntity);
            Thread.sleep(5000);
        }
        log.info("cloudx->openApi request, msg={} ", JSONObject.toJSONString(openApiEntity));
    }

    /**
     * 获取项目UUid
     *
     * @return 项目UUid
     */
    private String getProjectUUID() {
        Integer projectId = ProjectContextHolder.getProjectId();
        System.err.println("projectId = " + projectId);
        if (projectIdRelMap.containsKey(projectId)) {
            return (String) projectIdRelMap.get(projectId);
        } else {
            ProjectInfo projectInfo = projectInfoService.getById(projectId);
            System.err.println("projectInfo = " + JSONObject.toJSONString(projectInfo));
            if (projectInfo != null) {
                String uuid = projectInfoService.getProjectUUID(projectId);
                System.err.println("projectUUID = " + uuid);
                if (StrUtil.isNotEmpty(uuid)) {
                    projectIdRelMap.put(projectId, uuid);
                }
                return uuid;
            } else {
                return null;
            }
        }
    }
}
