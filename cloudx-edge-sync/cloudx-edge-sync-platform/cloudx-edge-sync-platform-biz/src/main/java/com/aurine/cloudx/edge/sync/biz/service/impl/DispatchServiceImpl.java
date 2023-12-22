package com.aurine.cloudx.edge.sync.biz.service.impl;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.edge.sync.biz.config.ImageConfig;
import com.aurine.cloudx.edge.sync.biz.config.SkipConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.DispatchService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.biz.service.handler.ImageToBase64Chain;
import com.aurine.cloudx.edge.sync.biz.thread.EventSendThread;
import com.aurine.cloudx.edge.sync.biz.thread.SendThread;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public static Map<String,Integer> projectMap = new HashMap<>();

    @Resource
    private ImageConfig imageConfig;

    @Resource
    private SkipConfig skipConfig;

    /**
     * 不包含事件类型数据，事件类型数据调用eventDispatchQueue
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

        //跳过不处理的数据
        if (skipConfig.getServiceName().contains(taskInfo.getServiceName())
                && skipConfig.getProjectUuid().contains(taskInfo.getProjectUUID())) {
            taskInfo.setState(MessageSendStateEnum.SKIP.code);
            taskInfoService.updateById(taskInfo);
            return;
        }

        if (ObjectUtil.isNull(taskInfo)) {
            return;
        }
        // taskInfo转taskInfoD
        TaskInfoDto taskInfoDto = EntityChangeUtil.taskInfoToTaskInfoDto(taskInfo);
        // 添加oldMd5
        UuidRelation uuidRelation = uuidRelationService.getByUuid(taskInfoDto);

        if (uuidRelation != null) {
            taskInfoDto.setOldMd5(uuidRelation.getOldMd5());
        }

        try {
            //按base64传输图片的项目集合
            List<Integer> imageList = imageConfig.getImageList();
            Integer projectId = projectMap.get(projectUuid);
            if(projectId == null){
                projectId = taskInfoService.getProjectIdByProjectUuid(projectUuid);
                projectMap.put(projectUuid,projectId);
            }
            //包含按base64传输
            if(imageList.contains(projectId)){
                if(OperateTypeEnum.ADD.name.equals(taskInfoDto.getType()) &&
                        (ServiceNameEnum.FACE_INFO.name.equals(taskInfoDto.getServiceName())
                                || ServiceNameEnum.ALARM_EVENT.name.equals(taskInfoDto.getServiceName())
                                || ServiceNameEnum.SNAP_RECORD.name.equals(taskInfoDto.getServiceName())
                                || ServiceNameEnum.PERSON_ENTRANCE.name.equals(taskInfoDto.getServiceName()))){
                    imageToBase64Chain.doChain(taskInfoDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInfo.setErrMsg("发送端人脸转base64失败，错误信息：" + e.getMessage());
            taskInfoService.updateById(taskInfo);
            return;
        }

        // 添加msgId
        taskInfoDto.setMsgId(taskInfoDto.getTaskId());
        //写入队列
        QueueUtil.inToQueue(queueKey, taskInfoDto);

        //变更状态
        if (!taskInfo.getState().equals(MessageSendStateEnum.SENDING.code)) {
            taskInfo.setState(MessageSendStateEnum.SENDING.code);
            taskInfoService.updateById(taskInfo);
        }

        if (RedisUtil.hasKey(queueKey)) {
            log.info("{}消息调度完成", taskInfo.getServiceType());
        }

        if (OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(serviceType)) {
            eventSendThread.eventSendMessage();
        } else {
            sendThread.sendMessage();
        }
    }

}
