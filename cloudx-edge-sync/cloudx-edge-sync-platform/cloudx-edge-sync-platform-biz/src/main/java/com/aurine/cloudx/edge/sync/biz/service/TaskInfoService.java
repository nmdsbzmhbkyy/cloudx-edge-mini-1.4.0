/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.edge.sync.biz.service;


import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author pigx code generator
 * @date 2021-12-22 16:46:12
 */
public interface TaskInfoService extends IService<TaskInfo> {

    void intoQueueTaskInfo(TaskInfo taskInfo);

    /**
     * 新增/更新流水
     *
     * @param taskInfoList
     * @return TaskInfoResponse
     */
    void saveTaskInfo(List<TaskInfo> taskInfoList);


    /**
     * 根据字段查询流水表
     * @param taskInfo
     * tenantId 租户ID
     * projectUUID 项目UUID
     * uuid 业务uuid
     * serviceType 服务类型
     * serviceName 服务名称
     * operateType 操作类型
     *
     * @return TaskInfoResponse
     */
    List<TaskInfo> getUnSendTaskInfoByMarks(TaskInfo taskInfo);

    /**
     * 根据字段查询流水表
     * @param taskInfoDto
     * tenantId 租户ID
     * projectUUID 项目UUID
     * uuid 业务uuid
     * serviceType 服务类型
     * serviceName 服务名称
     * operateType 操作类型
     *
     * @return TaskInfoResponse
     */
    TaskInfo getSendingTaskInfoByMarks(TaskInfoDto taskInfoDto);

    /**
     * 获取要调度的UUID队列
     * @return
     */
    List<String> getDispatchQueueList();

    /**
     * 获取要调度的事件类型UUID队列
     * @return
     */
    List<String> getEventDispatchQueueList();

    /**
     * 获取要发送的UUID队列
     * @return
     */
    List<String> getSendQueueList();

    /**
     * 获取要发送的事件类型UUID队列
     * @return
     */
    List<String> getEventSendQueueList();

    /**
     * 校验newMd5是否已存在
     * @param projectUUID
     * @param serviceType
     * @param newMd5
     * @return
     */
    Boolean checkIsRepeatMessage(String projectUUID, String serviceType, String newMd5);

    /**
     * 根据taskId获取taskInfo信息
     * @param taskId
     * @return
     */
    TaskInfo getTaskInfoByTaskId(String taskId);

    /**
     * 根据uuid获取项目id
     *
     * @param projectUuid
     * @return
     */
    Integer getProjectIdByProjectUuid(String projectUuid);

    /**
     * 更新redis重试次数
     * @param taskId
     * @param projectUUID 项目uuid
     */
    void updateRedisRetryCount(String taskId, String projectUUID);

    Semaphore getSemaphore();
}
