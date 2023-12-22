package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.*;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealResponseService;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.*;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: wrm
 * @Date: 2022/01/20 9:08
 * @Package: com.aurine.cloudx.edge.sync.biz.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
@Slf4j
public class DealResponseServiceImpl implements DealResponseService {

    @Resource
    private ProjectRelationService projectRelationService;

    @Resource
    private UuidRelationService uuidRelationService;

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private DispatchService dispatchService;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 转换消息，
     * 判断消息成功失败,失败保存异常记录，中断操作
     * 新增操作更新关系表，删除操作删除关系表
     * 修改操作判断数据库对应数据发送状态是否已发送，不是则终止业务(从端被覆盖以及多次操作校验)
     * 更新数据库taskInfo数据
     * 删除redis消息队列消息
     */
    @Override
    public void dealResponse(TaskInfoDto taskInfoDto) {
        log.info("[{}] MQTT RECEIVE RESPONSE,taskId = {} ", taskInfoDto.getMsgId(), taskInfoDto.getTaskId());

        // 转换第三方projectUUID为自己的uuid,找不到关系不做后续处理
        Boolean changeProjectUuidResult = changeProjectUUIDToOwner(taskInfoDto);
        if (!changeProjectUuidResult) {
            return;
        }
        // 设置重试次数
        taskInfoDto.setRetriesCount((Integer) RedisUtil.hget(QueueUtil.getRetireCountKey(taskInfoDto.getProjectUUID()), taskInfoDto.getTaskId()));
        // 判断消息成功失败
        if (!checkResponseMessage(taskInfoDto)) {
            return;
        }

        // 分发操作
        switch (OpenPushSubscribeCallbackTypeEnum.getByName(taskInfoDto.getServiceType())) {
            case CASCADE:
                dealCascadeResponse(taskInfoDto);
                break;
            case EVENT:
            case OPERATE:
                dealCommonResponse(taskInfoDto);
                break;
            case COMMAND:
                dealCommandResponse(taskInfoDto);
                break;
            case OTHER:
                dealOtherResponse(taskInfoDto);
            default:
                break;
        }
    }

    //----------------------------级联业务--------------------------

    /**
     * 处理级联业务响应
     *
     * @param taskInfoDto
     */
    private void dealCascadeResponse(TaskInfoDto taskInfoDto) {
        dealResponseCascade(taskInfoDto);
        updateTaskInfoState(taskInfoDto);
    }

    /**
     * 级联处理
     * 级联 同意操作 新增projectRelation 关系表
     * 解绑 同意操作 删除projectRelation 关系表
     *
     * @param taskInfoDto
     */
    private void dealResponseCascade(TaskInfoDto taskInfoDto) {
        // 撤销申请，删除projectRelation关系，终止操作
        if (ServiceNameEnum.CASCADE_APPLY.name.equals(taskInfoDto.getServiceName())
                && CascadeTypeEnum.REVOKE.name.equals(taskInfoDto.getType())) {
            Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
            if (removeRes) {
                scribeCallbackService.deleteCascadeScribeCallback(taskInfoDto.getProjectUUID());
            }
        }
    }
    //----------------------------操作业务--------------------------

    /**
     * 处理操作业务响应
     *
     * @param taskInfoDto
     */
    private void dealCommonResponse(TaskInfoDto taskInfoDto) {
        // 获取uuid
        String uuid = getResponseUuid(taskInfoDto);
        if(StringUtil.isNotEmpty(uuid)) {
            // uuid关系处理，新增操作更新关系表，删除操作删除关系表,异常新增不保存关系
            dealUuidRelation(taskInfoDto);
            taskInfoDto.setUuid(taskInfoDto.getUuid());
        } else {
            // 转换传入的uuid为本地的uuid
            taskInfoDto.setUuid(uuid);
        }

        updateTaskInfoState(taskInfoDto);
    }

    private void dealOtherResponse(TaskInfoDto taskInfoDto) {
        log.info("dealOtherResponse{},", taskInfoDto);
        updateTaskInfoState(taskInfoDto);
    }

    /**
     * 获取Uuid关系表
     * 新增操作将thirdUuid覆盖uuid查询数据库，
     * 修改操作查询uuid关系表根据thirdUuid获取uuid
     * 删除操作根据uuid关系表根据uuid获取uuid
     *
     * @param taskInfoDto
     * @return
     */
    private String getResponseUuid(TaskInfoDto taskInfoDto) {
        // 新增操作
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            if (StrUtil.isNotEmpty(taskInfoDto.getThirdUuid())) {
                return taskInfoDto.getThirdUuid();
            } else {
                return null;
            }
        }
        // 修改操作
        else if (taskInfoDto.getType().equals(OperateTypeEnum.UPDATE.name)) {
            UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
            if (ObjectUtil.isNotEmpty(byThirdUuid) && StrUtil.isNotEmpty(byThirdUuid.getUuid())) {
                return byThirdUuid.getUuid();
            } else {
                taskInfoDto.setThirdUuid(taskInfoDto.getUuid());
                uuidRelationService.saveUuidRelation(taskInfoDto);
                return taskInfoDto.getUuid();
            }
        }
        // 删除操作
        else {
            if (StringUtil.isNotEmpty(taskInfoDto.getUuid())) {
                UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
                if (ObjectUtil.isNotEmpty(byThirdUuid) && StrUtil.isNotEmpty(byThirdUuid.getUuid())) {
                    return byThirdUuid.getUuid();
                }
            }
            return null;
        }
    }

    /**
     * 新增操作转换请求uuid与thirdUuid并保存uuid关系
     * 修改更新oldMd5值
     * 删除操作删除Uuid关系表uuid关系
     *
     * @param taskInfoDto
     */
    private Boolean dealUuidRelation(TaskInfoDto taskInfoDto) {
        // 新增操作转换请求uuid与thirdUuid并保存uuid关系
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
            // 已经存在此数据的异常数据，执行update方法更新md5，
            if (ObjectUtil.isNotEmpty(byThirdUuid)) {
                uuidRelationService.updateOldMd5ByThirdUuid(taskInfoDto);
                return true;
            }
            // 收到的uuid与thirdUuid转换存储
            String uuid = taskInfoDto.getThirdUuid();
            taskInfoDto.setThirdUuid(taskInfoDto.getUuid());
            taskInfoDto.setUuid(uuid);

            Boolean saveUuidRes = uuidRelationService.saveUuidRelation(taskInfoDto);
            if (!saveUuidRes) {
                log.info("发送端接收到的响应uuid关系保存失败，参数为{}", JSONObject.toJSONString(taskInfoDto));
            }
        }
        // 修改操作更新oldMd5
        if (taskInfoDto.getType().equals(OperateTypeEnum.UPDATE.name)) {
            Boolean saveUuidRes = uuidRelationService.updateOldMd5ByThirdUuid(taskInfoDto);
            if (!saveUuidRes) {
                log.info("发送端接收到的响应oldMd5更新失败，参数为{}", JSONObject.toJSONString(taskInfoDto));
            }
        }
        // 删除操作删除Uuid关系表uuid关系
        else if (taskInfoDto.getType().equals(OperateTypeEnum.DELETE.name)) {
            if(StringUtil.isNotEmpty(taskInfoDto.getUuid())) {
                uuidRelationService.deleteByThirdUuid(taskInfoDto);
            }
        }
        return true;
    }

    //----------------------------命令业务--------------------------

    /**
     * 处理命令业务响应
     *
     * @param taskInfoDto
     */
    private void dealCommandResponse(TaskInfoDto taskInfoDto) {
        updateTaskInfoState(taskInfoDto);
    }

    //----------------------------公共业务--------------------------

    /**
     * @param request
     */
    private void updateTaskInfoState(TaskInfoDto request) {
        String queueKey = QueueUtil.getQueueKey(request.getServiceType(), request.getProjectUUID());
        // 是否正在发送消息列表中存在taskId
        boolean containTaskId = Objects.requireNonNull(RedisUtil.sGet(QueueUtil.getSendingTaskKey(request.getProjectUUID()))).contains(request.getTaskId());

        TaskInfoDto redisTaskInfoDto = (TaskInfoDto) RedisUtil.get(queueKey);
        TaskInfo currentTaskInfo = null;
        if (redisTaskInfoDto == null) {
            // 存在发送消息列表中则获取对应消息
            if (containTaskId) {
                currentTaskInfo = taskInfoService.getTaskInfoByTaskId(request.getTaskId());
            }
        } else {
            JSONObject jsonData = JSONObject.parseObject(redisTaskInfoDto.getData());
            if (jsonData.containsKey(PublicConstants.PIC_BASE64_KEY)) {
                jsonData.remove(PublicConstants.PIC_BASE64_KEY);
                redisTaskInfoDto.setData(jsonData.toString());
            }
            currentTaskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(redisTaskInfoDto);
        }

        if (currentTaskInfo == null) {
            log.warn("updateTaskInfoState 未获取到当前任务对应流水记录：taskId = {}", request.getTaskId());
            return;
        }

        // 更新发送状态为已完成
        currentTaskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        currentTaskInfo.setUpdateTime(LocalDateTime.now());
        boolean updateSuccess = taskInfoService.updateById(currentTaskInfo);

        //清空指令 清空uuid关系
        if (currentTaskInfo.getType().equals(CommandTypeEnum.EMPTY.name)) {
            uuidRelationService.remove(Wrappers.lambdaQuery(UuidRelation.class).eq(UuidRelation::getProjectUUID, currentTaskInfo.getProjectUUID()));
        }

        /**
         *  删除redis数据库中 队列uuid的数据
         *  删除完成后触发下一条数据入发送队列
         */
        if (RedisUtil.hasKey(queueKey) && updateSuccess) {
            Boolean delRes = RedisUtil.del(queueKey);
            if (delRes) {
                sendNext(currentTaskInfo.getServiceType(), currentTaskInfo.getProjectUUID(), currentTaskInfo.getTaskId());
            }
        }

        log.info("业务处理完成");
    }

    /**
     * 判断消息返回结果，失败保存失败信息
     *
     * @param taskInfoDto
     * @return
     */
    private Boolean checkResponseMessage(TaskInfoDto taskInfoDto) {
        if (taskInfoDto.getErrMsg() != null) {
            log.info("[{}] 发送端接收到的响应结果有误，错误信息为{}", taskInfoDto.getMsgId(), taskInfoDto.getErrMsg());
            String queueKey = QueueUtil.getQueueKey(taskInfoDto.getServiceType(), taskInfoDto.getProjectUUID());

            //请求错误时保存错误信息到数据库
            TaskInfo currentTaskInfo;
            TaskInfoDto redisTaskInfoDto = (TaskInfoDto) RedisUtil.get(queueKey);

            if (redisTaskInfoDto == null) {
                currentTaskInfo = taskInfoService.getSendingTaskInfoByMarks(taskInfoDto);
                if (ObjectUtil.isEmpty(currentTaskInfo)) {
                    log.error("[{}] taskId为[{}]的数据在redis和数据库发送中消息中找不到对应消息数据", taskInfoDto.getMsgId(), taskInfoDto.getTaskId());
                    return false;
                }
            } else {
                JSONObject jsonData = JSONObject.parseObject(redisTaskInfoDto.getData());
                if (jsonData.containsKey(PublicConstants.PIC_BASE64_KEY)) {
                    jsonData.remove(PublicConstants.PIC_BASE64_KEY);
                    redisTaskInfoDto.setData(jsonData.toString());
                }
                currentTaskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(redisTaskInfoDto);
                currentTaskInfo.setState(MessageSendStateEnum.SENDING.code);
            }

            currentTaskInfo.setUpdateTime(LocalDateTime.now());
            currentTaskInfo.setLastErrTime(LocalDateTime.now());
            currentTaskInfo.setErrMsg(taskInfoDto.getErrMsg());
            // 消息错误处理
            if (this.filterError(taskInfoDto)) {
                currentTaskInfo.setState(MessageSendStateEnum.COMPLETE.code);
                taskInfoService.updateById(currentTaskInfo);
            } else {
                // 业务错误更新状态为错误然后继续业务
                currentTaskInfo.setState(MessageSendStateEnum.ERROR.code);
                taskInfoService.updateById(currentTaskInfo);
            }
            if (RedisUtil.hasKey(queueKey)) {
                Boolean delRes = RedisUtil.del(queueKey);
                if (delRes) {
                    sendNext(currentTaskInfo.getServiceType(), currentTaskInfo.getProjectUUID(), currentTaskInfo.getTaskId());
                    log.info("[{}] [错误响应处理完成]， taskId = {},message = {}", taskInfoDto.getMsgId(), taskInfoDto.getTaskId(), taskInfoDto);
                }
            } else {
                log.info("[{}] [错误响应处理异常，redis删除失败] taskId = {},message = {}", taskInfoDto.getMsgId(), taskInfoDto.getTaskId(), taskInfoDto);
            }
            return false;
        }
        return true;
    }

    private Boolean filterError(TaskInfoDto taskInfoDto) {
        //临时方法
        try {
            Result r = JSONObject.parseObject(taskInfoDto.getErrMsg(), Result.class);
            if (r.getCode() == CloudxOpenErrorEnum.EMPTY_RESULT.getCode()) {
                return true;
            }
            if (r.getCode() == CloudxOpenErrorEnum.REPEATING_DATA.getCode()) {
                return true;
            }
        } catch (Exception e) {
            log.info("类型转换出错{}", e.getMessage());
            return false;
        } finally {
        }
        return false;
    }

    /**
     * 修改第三方projectUUID为自己的projectUUID
     *
     * @param taskInfoDto
     */
    private Boolean changeProjectUUIDToOwner(TaskInfoDto taskInfoDto) {
        // 根据第三方ProjectUuid获取ProjectUuid
        ProjectRelation byProjectCode = projectRelationService.getByProjectCode(taskInfoDto.getProjectUUID());
        if (byProjectCode == null) {
            log.error("response转换projectUUID失败");
            return false;
        }
        taskInfoDto.setProjectUUID(byProjectCode.getProjectUUID());
        return true;
    }

    /**
     * 消息队发送下一条消息
     */
    private void sendNext(String serviceType, String projectUuid, String taskId) {
        try {
            // 调度线程调度下条消息
            dispatchService.dispatchQueue(serviceType, projectUuid);
        } catch (Exception e) {
            log.error("生成下发请求数据异常, projectUuid:{}", projectUuid, e);
        }
        // 删除正在发送列表taskId值
        RedisUtil.setRemove(QueueUtil.getSendingTaskKey(projectUuid), taskId);
        // 删除taskId重试次数
        RedisUtil.hdel(QueueUtil.getRetireCountKey(projectUuid), taskId);
    }
}
