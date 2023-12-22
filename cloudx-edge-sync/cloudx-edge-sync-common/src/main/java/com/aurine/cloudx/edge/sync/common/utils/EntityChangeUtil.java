package com.aurine.cloudx.edge.sync.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wrm
 * @Date: 2022/01/16 15:31
 * @Package: com.aurine.cloudx.edge.sync.biz.util
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
public class EntityChangeUtil {

    /**
     * TaskInfo转taskInfoDto
     *
     * @param taskInfo
     * @return taskInfoDto
     */
    public static TaskInfoDto taskInfoToTaskInfoDto(TaskInfo taskInfo) {
        TaskInfoDto taskInfoDto = new TaskInfoDto();
        BeanUtil.copyProperties(taskInfo, taskInfoDto);
        String serviceType = taskInfo.getServiceType();
        if ("cascade".equals(serviceType)) {
            log.info("级联入云获取第三方项目ID：{}", taskInfo);
            String data = taskInfo.getData();
            try {
                JsonNode jsonNode = ObjectMapperUtil.instance().readTree(data);
                JsonNode projectCode = jsonNode.findPath("projectCode");
                if (!projectCode.isMissingNode()) {
                    taskInfoDto.setProjectCode(projectCode.asText());
                } else {
                    log.info("级联入云未获取到第三方项目ID：{}", taskInfo);
                }
            } catch (JsonProcessingException e) {
                log.error("级联入云获取第三方项目ID失败", e);
                e.printStackTrace();
            }
        }
        return taskInfoDto;
    }

    /**
     * TaskInfo转taskInfoDto
     *
     * @param taskInfoDto
     * @return taskInfoDto
     */
    public static TaskInfo taskInfoDtoToTaskInfo(TaskInfoDto taskInfoDto) {
        TaskInfo taskInfo = new TaskInfo();
        BeanUtil.copyProperties(taskInfoDto, taskInfo);
        return taskInfo;
    }


    /**
     * OpenApiEntity转TaskInfo
     *
     * @param openApiEntity
     * @return
     */
    public static TaskInfo openApiEntityToTaskInfo(OpenApiEntity openApiEntity) {
        TaskInfo taskInfo = new TaskInfo();
        BeanUtil.copyProperties(openApiEntity, taskInfo, "data");
        taskInfo.setUuid(openApiEntity.getEntityId());
        taskInfo.setType(openApiEntityTypeToTaskInfoType(openApiEntity));
        taskInfo.setData(JSONObject.toJSONString(openApiEntity.getData()));
        return taskInfo;
    }

    /**
     * TaskInfoDto转OpenApiEntity
     *
     * @param taskInfoDto
     * @return
     */
    public static OpenApiEntity TaskInfoDtoToToOpenApiEntity(TaskInfoDto taskInfoDto) {
        OpenApiEntity openApiEntity = new OpenApiEntity();
        BeanUtil.copyProperties(taskInfoDto, openApiEntity, "data");
        openApiEntity.setData(JSONObject.parseObject(taskInfoDto.getData()));
        openApiEntity.setEntityId(taskInfoDto.getUuid());
        OpenApiEntity resOpenApiEntity = taskInfoDtoTypeToOpenApiEntityType(taskInfoDto, openApiEntity);
        return resOpenApiEntity;
    }

    private static <T extends OpenApiEntity> String openApiEntityTypeToTaskInfoType(T t) {
        switch (OpenPushSubscribeCallbackTypeEnum.getByName(t.getServiceType())) {
            case OPERATE:
            case OTHER:
            case EVENT:
                return t.getOperateType();
            case CASCADE:
                return t.getCascadeType();
            case COMMAND:
                return t.getCommandType();
            default:
                return null;
        }
    }

    private static <T extends OpenApiEntity> T taskInfoDtoTypeToOpenApiEntityType(TaskInfoDto taskInfo, T openApiEntity) {
        switch (OpenPushSubscribeCallbackTypeEnum.getByName(taskInfo.getServiceType())) {
            case OPERATE:
            case EVENT:
                openApiEntity.setOperateType(taskInfo.getType());
                break;
            case CASCADE:
                openApiEntity.setCascadeType(taskInfo.getType());
                break;
            case COMMAND:
                openApiEntity.setCommandType(taskInfo.getType());
                break;
            default:
                break;
        }
        return openApiEntity;
    }
}
