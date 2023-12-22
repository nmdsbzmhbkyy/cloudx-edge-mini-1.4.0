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
package com.aurine.cloudx.edge.sync.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.mapper.TaskInfoMapper;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.constant.QueueConstant;
import com.aurine.cloudx.edge.sync.common.constant.SqlCacheConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.aurine.cloudx.edge.sync.biz.constant.Constants.SYNC_DISPATCH_TASK_INFO_LOCK_PREFIX;

/**
 * @author pigx code generator
 * @date 2021-12-22 16:46:12
 */
@Service
@Slf4j
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService {

    @Resource
    private TaskInfoMapper taskInfoMapper;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ProjectRelationService projectRelationService;

    private static Semaphore semaphore = new Semaphore(1);

    @Resource
    private RedissonClient redissonClient;

    /**
     * 新增流水存入缓存中，由线程进行消费，高并发下实现数据缓存入redis
     * @param taskInfo
     * @return
     */
    //@SneakyThrows
    @Override
    public void intoQueueTaskInfo(TaskInfo taskInfo) {
        redisTemplate.opsForList().rightPush(SqlCacheConstants.SQL_CACHE_KEY, taskInfo);
    }

    @Override
    public void saveTaskInfo(List<TaskInfo> taskInfoList) {
        log.info("批量入库：{}", taskInfoList);
        List<TaskInfo> saveTaskInfoList = new ArrayList<>();
        for (TaskInfo taskInfo : taskInfoList) {
            try {
                // 判断传入的数据的projectUUID是否存在关系表中，不存在则忽略此数据
                ProjectRelation projectRelation = projectRelationService.getByProjectUUID(taskInfo.getProjectUUID());
                if (ObjectUtil.isEmpty(projectRelation)) {
                    continue;
                }

                // 收到的重复消息不做处理
                Boolean isRepeatMessage = checkIsRepeatMessage(taskInfo.getProjectUUID(), taskInfo.getServiceType(), taskInfo.getNewMd5());
                if (isRepeatMessage) {
                    continue;
                }
                // 操作类型 - 更新操作 过期历史相同uuid且未发送的历史消息
                expiredUpdateUnSendMessage(taskInfo);
                // 添加待入库数据
                saveTaskInfoList.add(taskInfo);
            } catch (Exception e) {
                log.error("批量入库数据检查异常 {}", taskInfo, e);
            }
        }
        if (saveTaskInfoList.size()>0) {
            // 消息批量入库
            super.saveBatch(saveTaskInfoList);
        }
    }

    @Override
    public List<TaskInfo> getUnSendTaskInfoByMarks(TaskInfo taskInfo) {
        taskInfo.setState(MessageSendStateEnum.WAIT.code);
        List<TaskInfo> collect = taskInfoMapper.getTaskInfoByMarks(taskInfo);
        if (collect.size() > 1) {
            log.info("UnSendTaskInfo 异常,查到多条匹配数据,请求参数为{}", JSONObject.toJSONString(taskInfo));
        } else if (collect.size() == 0) {
            return null;
        }
        return collect;
    }

    @Override
    public TaskInfo getSendingTaskInfoByMarks(TaskInfoDto taskInfoDto) {
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        taskInfo.setState(MessageSendStateEnum.SENDING.code);
        List<TaskInfo> collect = taskInfoMapper.getTaskInfoByMarks(taskInfo);
        if (collect.size() > 1) {
            log.info("SendingTaskInfo 异常,查到多条匹配数据,请求参数为{}", JSONObject.toJSONString(taskInfo));
        } else if (collect.size() == 0) {
            return null;
        }
        return collect.get(0);
    }

    @Override
    public String  getDispatchQueueList() {
        return taskInfoMapper.getDispatchQueueList();
    }

    @Override
    public String getEventDispatchQueueList() {
        return taskInfoMapper.getEventDispatchQueueList();
    }

    @Override
    public String getSendQueueList() {
        return taskInfoMapper.getSendQueueList();
    }

    @Override
    public String getEventSendQueueList() {
        return taskInfoMapper.getEventSendQueueList();
    }

    @Override
    public Boolean checkIsRepeatMessage(String projectUUID, String serviceType, String newMd5) {
        TaskInfo newestTaskInfo = getNewestTaskInfo(projectUUID, serviceType, Constants.SOURCE);
        if (ObjectUtil.isNotEmpty(newestTaskInfo)) {
            return newMd5.equals(newestTaskInfo.getNewMd5());
        }
        return false;
    }

    @Override
    public TaskInfo getTaskInfoByTaskId(String taskId) {
        LambdaQueryWrapper<TaskInfo> queryWrapper = new QueryWrapper<TaskInfo>().lambda()
                .eq(TaskInfo::getTaskId, taskId)
                .isNull(TaskInfo::getErrMsg)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    private TaskInfo getNewestTaskInfo(String projectUUID, String serviceType, String source) {
        LambdaQueryWrapper<TaskInfo> queryWrapper = new QueryWrapper<TaskInfo>().lambda()
                .eq(TaskInfo::getProjectUUID, projectUUID)
                .eq(TaskInfo::getServiceType, serviceType)
                .ne(TaskInfo::getSource, source)
                .orderByDesc(TaskInfo::getSeq)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }


    /**
     * 操作类型 - 更新操作 过期历史相同uuid且未发送的历史消息
     * 使用信号量，避免修改操作过期旧数据导致脏读影响正常业务
     */
    @SneakyThrows
    private void expiredUpdateUnSendMessage(TaskInfo taskInfo) {
        boolean isOperate = OpenPushSubscribeCallbackTypeEnum.OPERATE.name.equals(taskInfo.getServiceType());
        boolean isUpdate = OperateTypeEnum.UPDATE.name.equals(taskInfo.getType());
        if (isUpdate && isOperate) {
            // uuid不存在跳过过期操作
            if (StringUtil.isEmpty(taskInfo.getUuid())) {
                return;
            }
            RLock lock = redissonClient.getLock(SYNC_DISPATCH_TASK_INFO_LOCK_PREFIX +  taskInfo.getProjectUUID());
            if (lock!=null) {
                lock.lock();
                try {
                    List<TaskInfo> taskInfoList = getUnSendTaskInfoByMarks(taskInfo);
                    if (CollUtil.isNotEmpty(taskInfoList)) {
                        taskInfoList.forEach((curr) -> {
                            curr.setState(MessageSendStateEnum.EXPIRED.code);
                        });
                        // 批量过期数据
                        updateBatchById(taskInfoList);
                    }
                } catch (Exception e) {
                    log.error("批量过期数据异常", e);
                } finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        }
    }

    /**
     * 更新redis重试次数
     * @param taskId
     */
    @Override
    public void updateRedisRetryCount(String taskId, String projectUUID) {
        RedisUtil.syncLock(QueueConstant.QUEUE_LOCK, QueueConstant.QUEUE_LOCK_TTL);
        String retireCountKey = QueueUtil.getRetireCountKey(projectUUID);
        Object retryCount = RedisUtil.hget(retireCountKey, taskId);
        if (ObjectUtil.isEmpty(retryCount)) {
            RedisUtil.hset(retireCountKey,taskId,0);
            retryCount = 0;
        }
        RedisUtil.hset(retireCountKey, taskId, Integer.parseInt(retryCount.toString()) + 1);
        RedisUtil.unLock(QueueConstant.QUEUE_LOCK);
    }

}
