package com.aurine.cloudx.edge.sync.biz.service.impl;

import com.aurine.cloudx.edge.sync.biz.service.EdgeSyncTransferService;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: wrm
 * @Date: 2022/01/06 9:04
 * @Package: com.aurine.cloudx.edge.sync.biz.transfar.toedgesync
 * @Version: 1.0
 * @Remarks: 通过http请求向的另一个同步服务发送消息
 **/
@Service
@Configuration
public class EdgeSyncTransferServiceImpl implements EdgeSyncTransferService {

    /**
     * 发送事件消息
     */
    @Async
    @Override
    public void sendEventToEdgeSync(OpenApiEntity openApiEntity) {
    }

    /**
     * 发送操作消息
     */
    @Async
    @Override
    public void sendOperateToEdgeSync(OpenApiEntity openApiEntity) {
    }

    /**
     * 发送命令消息
     */
    @Async
    @Override
    public void sendCommandToEdgeSync(OpenApiEntity openApiEntity) {
    }


    /**
     * 发送其他消息
     */
    @Async
    @Override
    public void sendOtherToEdgeSync(OpenApiEntity openApiEntity) {
    }

    @Override
    public void sendEventToEdgeSync(TaskInfoDto taskInfoDto) {
        OpenApiEntity openApiEntity = EntityChangeUtil.TaskInfoDtoToToOpenApiEntity(taskInfoDto);
        sendEventToEdgeSync(openApiEntity);
    }

    @Override
    public void sendOperateToEdgeSync(TaskInfoDto taskInfoDto) {
        OpenApiEntity openApiEntity = EntityChangeUtil.TaskInfoDtoToToOpenApiEntity(taskInfoDto);
        sendOperateToEdgeSync(openApiEntity);
    }

    @Override
    public void sendCommandToEdgeSync(TaskInfoDto taskInfoDto) {
        OpenApiEntity openApiEntity = EntityChangeUtil.TaskInfoDtoToToOpenApiEntity(taskInfoDto);
        sendCommandToEdgeSync(openApiEntity);
    }

    @Override
    public void sendOtherToEdgeSync(TaskInfoDto taskInfoDto) {
        OpenApiEntity openApiEntity = EntityChangeUtil.TaskInfoDtoToToOpenApiEntity(taskInfoDto);
        sendOtherToEdgeSync(openApiEntity);
    }
}
