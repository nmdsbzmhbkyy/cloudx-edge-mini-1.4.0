package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.biz.service.biz.OpenApiPushService;
import com.aurine.cloudx.edge.sync.biz.thread.DispatchThread;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 8:46
 * @Package: com.aurine.cloudx.edge.sync.biz.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
@Slf4j
public class OpenApiPushServiceImpl implements OpenApiPushService {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private ProjectRelationService projectRelationService;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    @Resource
    private UuidRelationService uuidRelationService;

    @Override
    public Boolean dealConfigRequest(OpenApiEntity requestObj) {
        //保存projectRelation关系
        ProjectRelation projectRelation = new ProjectRelation();
        projectRelation.setProjectUUID(requestObj.getData().getString("projectCode"));
        projectRelation.setProjectCode(requestObj.getData().getString("projectCode"));
        log.info("申请级联,projectUUID为{}", projectRelation.getProjectUUID());
        Boolean addRes = projectRelationService.addProjectRelation(projectRelation);
        if (addRes) {
            // 添加cascade订阅
            scribeCallbackService.addCascadeScribeCallback(requestObj.getProjectUUID());
        }
        return true;
    }

    @Override
    public Boolean dealEventRequest(OpenApiEntity requestObj) {
        // 保存消息
        TaskInfo taskInfo = saveMessage(requestObj);
        if (taskInfo == null) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean dealOperateRequest(OpenApiEntity requestObj) {
        // 保存消息
        TaskInfo taskInfo = saveMessage(requestObj);
        if (taskInfo == null) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean dealCommandRequest(OpenApiEntity requestObj) {
        // 保存消息
        TaskInfo taskInfo = saveMessage(requestObj);
        if (taskInfo == null) {
            return false;
        }
        return true;
    }

    /**
     * 处理级联请求, 不同端代码不一致
     *
     * @param requestObj
     * @return
     */
    @Override
    public Boolean dealCascadeRequest(OpenApiEntity requestObj) {
        // 保存消息
        TaskInfo taskInfo = saveMessage(requestObj);
        if (taskInfo == null) {
            return false;
        }
        return true;
    }

    /**
     * 处理其他请求
     *
     * @param requestObj
     * @return
     */
    @Override
    public Boolean dealOtherRequest(OpenApiEntity requestObj) {
        TaskInfo taskInfo = saveMessage(requestObj);
        if (taskInfo == null) {
            return false;
        }
        return true;
    }

    /**
     * 统一将请求信息存入流水表,交由消息队列发送消息
     *
     * @param requestObj
     * @return
     */
    private TaskInfo saveMessage(OpenApiEntity requestObj) {
        // 判断传入的数据的projectUUID是否存在关系表中，不存在则忽略此数据
        ProjectRelation projectRelation = projectRelationService.getByProjectUUID(requestObj.getProjectUUID());
        if (ObjectUtil.isEmpty(projectRelation)) {
            return null;
        }

        TaskInfo taskInfo = EntityChangeUtil.openApiEntityToTaskInfo(requestObj);
        String requestString = JSON.toJSONString(requestObj);
        String newMd5 = DigestUtils.md5DigestAsHex(requestString.getBytes());

        // 收到的重复消息不做处理
        Boolean isRepeatMessage = taskInfoService.checkIsRepeatMessage(taskInfo.getProjectUUID(), taskInfo.getServiceType(), newMd5);
        if (isRepeatMessage) {
            return taskInfo;
        }

        //设置newMd5码
        taskInfo.setNewMd5(newMd5);
        //设置来源
        if (requestObj.getSource() == null) {
            taskInfo.setSource(Constants.SOURCE);
        }
        taskInfoService.saveTaskInfo(taskInfo);

        log.info("saveMessage Res = {}", taskInfo);
        return taskInfo;
    }

}
