package com.aurine.cloudx.edge.sync.biz.service;

import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;

/**
 * @Author: wrm
 * @Date: 2022/01/20 10:59
 * @Package: com.aurine.cloudx.edge.sync.biz.transfer.http
 * @Version: 1.0
 * @Remarks:
 **/
public interface EdgeSyncTransferService {
    /**
     * 转发事件消息
     * @param openApiEntity
     */
    void sendEventToEdgeSync(OpenApiEntity openApiEntity);

    /**
     * 转发操作消息
     * @param openApiEntity
     */
    void sendOperateToEdgeSync(OpenApiEntity openApiEntity);

    /**
     * 转发命令消息
     * @param openApiEntity
     */
    void sendCommandToEdgeSync(OpenApiEntity openApiEntity);

    /**
     * 转发其他消息
     * @param openApiEntity
     */
    void sendOtherToEdgeSync(OpenApiEntity openApiEntity);

    /**
     * 转发事件消息
     * @param taskInfoDto
     */
    void sendEventToEdgeSync(TaskInfoDto taskInfoDto);

    /**
     * 转发操作消息
     * @param taskInfoDto
     */
    void sendOperateToEdgeSync(TaskInfoDto taskInfoDto);

    /**
     * 转发命令消息
     * @param taskInfoDto
     */
    void sendCommandToEdgeSync(TaskInfoDto taskInfoDto);

    /**
     * 转发其他消息
     * @param taskInfoDto
     */
    void sendOtherToEdgeSync(TaskInfoDto taskInfoDto);

  
}
