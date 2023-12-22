package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
        // 转换第三方projectUUID为自己的uuid
        changeProjectUUIDToOwner(taskInfoDto);
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
        if (ServiceNameEnum.CASCADE_APPLY.name.equals(taskInfoDto.getServiceName())) {
            switch (CascadeTypeEnum.getByName(taskInfoDto.getType())) {
                case ACCEPT:
                    // 同意级联申请,订阅事件，操作，命令，其他类型消息
                    scribeCallbackService.addScribeCallbackWithoutCascade(taskInfoDto.getProjectUUID());
                    break;
                case REJECT:
                    // 拒绝级联申请,删除project_relation关系，删除级联订阅，删除mqtt订阅
                    Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
                    if (removeRes) {
                        scribeCallbackService.deleteCascadeScribeCallback(taskInfoDto.getProjectUUID());
                    }
                    break;
                default:
                    break;
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
        // uuid关系处理，新增操作更新关系表，删除操作删除关系表
        boolean dealUuidRelationResult = dealUuidRelation(taskInfoDto);
        if (!dealUuidRelationResult) {
            return;
        }
        // 转换传入的uuid为本地的uuid
        taskInfoDto.setUuid(uuid);
        updateTaskInfoState(taskInfoDto);
    }

    private void dealOtherResponse(TaskInfoDto taskInfoDto) {
        log.info("dealOtherResponse{},", taskInfoDto);
        updateTaskInfoState(taskInfoDto);
    }

    /**
     * 获取Uuid关系表
     * 新增操作将thirdUuid覆盖uuid查询数据库，
     * 修改删除操作查询uuid关系表获取uuid
     *
     * @param taskInfoDto
     * @return
     */
    private String getResponseUuid(TaskInfoDto taskInfoDto) {
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            return taskInfoDto.getThirdUuid();
        } else {
            UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
            return byThirdUuid.getUuid();
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
                return false;
            }
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
            Boolean deleteUuidRes = uuidRelationService.deleteByThirdUuid(taskInfoDto);
            if (!deleteUuidRes) {
                log.info("发送端接收到的响应uuid关系删除失败，参数为{}", JSONObject.toJSONString(taskInfoDto));
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

        TaskInfoDto redisTaskInfoDto = (TaskInfoDto) RedisUtil.get(queueKey);
        TaskInfo currentTaskInfo;
        if (redisTaskInfoDto == null) {
            currentTaskInfo = taskInfoService.getSendingTaskInfoByMarks(request);
        } else {
            JSONObject jsonData = JSONObject.parseObject(redisTaskInfoDto.getData());
            if (jsonData.containsKey(PublicConstants.PIC_BASE64_KEY)) {
                jsonData.remove(PublicConstants.PIC_BASE64_KEY);
                redisTaskInfoDto.setData(jsonData.toString());
            }
            currentTaskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(redisTaskInfoDto);
        }
        if (currentTaskInfo == null) {
            log.warn("updateTaskInfoState 未获取到当前任务对应流水记录：{}", JSON.toJSONString(request));
            return;
        }
        //清空指令 清空uuid关系
        if (currentTaskInfo.getType().equals(CommandTypeEnum.EMPTY.name)) {
            uuidRelationService.remove(Wrappers.lambdaQuery(UuidRelation.class).eq(UuidRelation::getProjectUUID, currentTaskInfo.getProjectUUID()));
        }
        // 更新发送状态为已完成
        currentTaskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        currentTaskInfo.setUpdateTime(LocalDateTime.now());
        taskInfoService.updateById(currentTaskInfo);
        /**
         *  删除redis数据库中 队列uuid的数据
         *  删除完成后触发下一条数据入发送队列
         */
        if (RedisUtil.hasKey(queueKey)) {
            Boolean delRes = RedisUtil.del(queueKey);
            if (delRes) {
                dispatchService.dispatchQueue(request.getServiceType(), currentTaskInfo.getProjectUUID());
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
            String queueKey = QueueUtil.getQueueKey(taskInfoDto.getServiceType(), taskInfoDto.getProjectUUID());

            //请求错误时保存错误信息到数据库
            TaskInfo currentTaskInfo = null;
            TaskInfoDto redisTaskInfoDto = (TaskInfoDto) RedisUtil.get(queueKey);
            if (redisTaskInfoDto == null) {
                currentTaskInfo = taskInfoService.getSendingTaskInfoByMarks(taskInfoDto);
            } else {
                JSONObject jsonData = JSONObject.parseObject(redisTaskInfoDto.getData());
                if (jsonData.containsKey(PublicConstants.PIC_BASE64_KEY)) {
                    jsonData.remove(PublicConstants.PIC_BASE64_KEY);
                    redisTaskInfoDto.setData(jsonData.toString());
                }
                currentTaskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(redisTaskInfoDto);
                currentTaskInfo.setState(MessageSendStateEnum.SENDING.code);
            }
            currentTaskInfo.setLastErrTime(LocalDateTime.now());
            currentTaskInfo.setErrMsg(taskInfoDto.getErrMsg());
            if (this.filterError(taskInfoDto)) {
                log.info("filterError,{}", taskInfoDto);
                currentTaskInfo.setState(MessageSendStateEnum.COMPLETE.code);
                if (RedisUtil.hasKey(queueKey)) {
                    Boolean delRes = RedisUtil.del(queueKey);
                    if (delRes) {
                        dispatchService.dispatchQueue(currentTaskInfo.getServiceType(), currentTaskInfo.getProjectUUID());
                    }
                }
            }
            ;
            // TODO: 2022/3/14 异常内容具体处理 比如state转为2等根据不同异常具体操作，额外开一个方法
            taskInfoService.updateById(currentTaskInfo);
            log.info("发送端接收到的响应结果有误，结果为{}", taskInfoDto.getErrMsg());
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
            if(r.getCode() == CloudxOpenErrorEnum.REPEATING_DATA.getCode()) {
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
    private void changeProjectUUIDToOwner(TaskInfoDto taskInfoDto) {
        // 根据第三方ProjectUuid获取ProjectUuid
        ProjectRelation byProjectCode = projectRelationService.getByProjectCode(taskInfoDto.getProjectUUID());
        if (byProjectCode == null) {
            log.error("response转换projectUUID失败");
        }
        taskInfoDto.setProjectUUID(byProjectCode.getProjectUUID());
    }
}
