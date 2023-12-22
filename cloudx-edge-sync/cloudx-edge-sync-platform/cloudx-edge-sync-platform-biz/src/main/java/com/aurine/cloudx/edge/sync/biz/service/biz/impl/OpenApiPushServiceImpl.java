package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.service.biz.OpenApiPushService;
import com.aurine.cloudx.edge.sync.common.entity.OpenApiEntity;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.concurrent.Semaphore;

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

    @Override
    public Boolean dealConfigRequest(OpenApiEntity requestObj) {
        //保存projectRelation关系，不同端代码不一致
        ProjectRelation projectRelation = new ProjectRelation();
        projectRelation.setProjectUUID(requestObj.getProjectUUID());
        projectRelation.setProjectCode(requestObj.getData().getString("projectCode"));
        projectRelation.setSn(requestObj.getData().getString("edgeDeviceId"));
        projectRelation.setSyncType(requestObj.getData().getInteger("syncType"));

        log.info("申请级联,projectUUID为{}", projectRelation.getProjectUUID());
        Boolean addRes = projectRelationService.addProjectRelation(projectRelation);
        if (addRes) {
            // 添加cascade订阅
            scribeCallbackService.addCascadeScribeCallback(requestObj.getProjectUUID());
            // 添加信号量
            InitConfig.addSemaphore(requestObj.getProjectUUID());
        }
        return true;
    }

    @Override
    public Boolean dealEventRequest(OpenApiEntity requestObj) {
        return saveMessage(requestObj);
    }

    @Override
    public Boolean dealOperateRequest(OpenApiEntity requestObj) {
        return saveMessage(requestObj);
    }

    @Override
    public Boolean dealCommandRequest(OpenApiEntity requestObj) {
        return saveMessage(requestObj);
    }

    /**
     * 处理入云请求, 不同端代码不一致
     *
     * @param requestObj
     * @return
     */
    @Override
    public Boolean dealCascadeRequest(OpenApiEntity requestObj) {
        return saveMessage(requestObj);
    }

    /**
     * 处理其他请求
     *
     * @param requestObj
     * @return
     */
    @Override
    public Boolean dealOtherRequest(OpenApiEntity requestObj) {
        return saveMessage(requestObj);
    }


    /**
     * 统一将请求信息存入流水表,交由消息队列发送消息
     *
     * @param requestObj
     * @return
     */
    private Boolean saveMessage(OpenApiEntity requestObj) {

        TaskInfo taskInfo = EntityChangeUtil.openApiEntityToTaskInfo(requestObj);
        String requestString = JSON.toJSONString(requestObj);
        String newMd5 = DigestUtils.md5DigestAsHex(requestString.getBytes());

        // 设置newMd5码
        taskInfo.setNewMd5(newMd5);

        // 设置来源
        if (requestObj.getSource() == null) {
            taskInfo.setSource(Constants.SOURCE);
        }

        taskInfoService.intoQueueTaskInfo(taskInfo);

        log.info("saveMessage Redis = {}", taskInfo);
        return true;
    }

}
