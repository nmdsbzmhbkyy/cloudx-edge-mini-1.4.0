package com.aurine.cloudx.edge.sync.biz.service.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.edge.sync.biz.service.DispatchService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.biz.service.handler.ImageToBase64Chain;
import com.aurine.cloudx.edge.sync.biz.thread.EventSendThread;
import com.aurine.cloudx.edge.sync.biz.thread.SendThread;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.config.QueueConfig;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @description: 调度服务
 * @author: wangwei
 * @date: 2021/12/14 17:06
 **/
@Service
@Slf4j
public class DispatchServiceImpl implements DispatchService {

    @Resource
    ImageToBase64Chain imageToBase64Chain;

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private UuidRelationService uuidRelationService;

    @Resource
    private SendThread sendThread;

    @Resource
    private EventSendThread eventSendThread;

    /**
     * 根据调度策略，从数据库中获取下发缓存队列
     * 同步方法
     */
    @Override
    public synchronized void dispatchQueue(String serviceType, String projectUuid) {
        String queueKey = QueueUtil.getQueueKey(serviceType, projectUuid);
        //当队列存在数据的时候就不给他添加
        if (RedisUtil.hasKey(queueKey)) {
            return;
        }
        log.debug("开始调度 队列数量：{}", RedisUtil.get(queueKey));

        //方案策略实现
        //通过配置，获取队列数据
        LambdaQueryWrapper<TaskInfo> queryWrapper = new LambdaQueryWrapper<TaskInfo>().eq(TaskInfo::getProjectUUID, projectUuid).and
                (wp -> wp.eq(TaskInfo::getState, MessageSendStateEnum.WAIT.code).or()
                        .eq(TaskInfo::getState, MessageSendStateEnum.SENDING.code));

        if (OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(serviceType)) {
            queryWrapper.eq(TaskInfo::getServiceType, OpenPushSubscribeCallbackTypeEnum.EVENT.name);
        } else {
            queryWrapper.ne(TaskInfo::getServiceType, OpenPushSubscribeCallbackTypeEnum.EVENT.name);
        }
        queryWrapper.last("LIMIT 1");

        TaskInfo taskInfo = taskInfoService.getOne(queryWrapper);

        if (ObjectUtil.isNull(taskInfo)) {
            return;
        }
        // taskInfo转taskInfoD
        TaskInfoDto taskInfoDto = EntityChangeUtil.taskInfoToTaskInfoDto(taskInfo);
        // 添加msgId
        taskInfoDto.setMsgId(IdUtil.simpleUUID().toUpperCase());
        // 添加oldMd5
        UuidRelation uuidRelation = uuidRelationService.getByUuid(taskInfoDto);
        if (uuidRelation != null) {
            taskInfoDto.setOldMd5(uuidRelation.getOldMd5());
        }
        // 责任链判断是否需要图片转base64
        try {
            if (OperateTypeEnum.ADD.name.equals(taskInfoDto.getType())) {
                imageToBase64Chain.doChain(taskInfoDto);
            }
        } catch (Exception e) {
            taskInfoDto.setErrMsg("发送端人脸转base64失败，错误信息：" + e.getMessage());
            taskInfoService.updateById(taskInfo);
            return;
        }
        //写入队列
        QueueUtil.inToQueue(queueKey, taskInfoDto);

        //变更状态
        if(!taskInfo.getState().equals(MessageSendStateEnum.SENDING.code)) {
            taskInfo.setState(MessageSendStateEnum.SENDING.code);
            taskInfoService.updateById(taskInfo);
        }
        if (RedisUtil.hasKey(queueKey)) {
            log.info("调度完成 队列数量：{}", RedisUtil.get(queueKey));
        }

        if (OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(serviceType)) {
            eventSendThread.eventSendMessage();
        } else {
            sendThread.sendMessage();
        }
    }

}
