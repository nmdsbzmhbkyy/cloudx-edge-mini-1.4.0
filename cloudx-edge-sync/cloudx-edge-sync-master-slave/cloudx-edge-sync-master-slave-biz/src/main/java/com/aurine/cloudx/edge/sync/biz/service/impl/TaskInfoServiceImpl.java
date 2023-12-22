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
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.mapper.TaskInfoMapper;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.SqlCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public void saveTaskInfo(TaskInfo taskInfo) {
        //缓存操作
        long count = SqlCacheUtil.inTo(taskInfo);

        //检查缓存数量，缓存到一定的数量进行批量入库
        if (count >= globalVariable.getSqlCacheSize()) {
            List<TaskInfo> list = SqlCacheUtil.list();

            if (CollUtil.isNotEmpty(list)) {
                log.info("[清空SQL缓存] SQL超出阈值，执行入库操作");
                super.saveBatch(list);
            }
        }
    }

    @Override
    public TaskInfo getUnSendTaskInfoByMarks(TaskInfo taskInfo) {
        taskInfo.setState( MessageSendStateEnum.WAIT.code);
        List<TaskInfo> collect = taskInfoMapper.getTaskInfoByMarks(taskInfo);
        if (collect.size() > 1) {
            log.info("UnSendTaskInfo 异常,查到多条匹配数据,请求参数为{}", JSONObject.toJSONString(taskInfo));
        } else if (collect.size() == 0) {
            return null;
        }
        return collect.get(0);
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
    public List<String> getDispatchQueueList() {
        return taskInfoMapper.getDispatchQueueList();
    }

    @Override
    public List<String> getEventDispatchQueueList() {
        return taskInfoMapper.getEventDispatchQueueList();
    }

    @Override
    public List<String> getSendQueueList() {
        return taskInfoMapper.getSendQueueList();
    }

    @Override
    public List<String> getEventSendQueueList() {
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

    private TaskInfo getNewestTaskInfo(String projectUUID, String serviceType, String source) {
        LambdaQueryWrapper<TaskInfo> queryWrapper = new QueryWrapper<TaskInfo>().lambda()
                .eq(TaskInfo::getProjectUUID, projectUUID)
                .eq(TaskInfo::getServiceType, serviceType)
                .ne(TaskInfo::getSource, source)
                .orderByDesc(TaskInfo::getSeq)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }
}
