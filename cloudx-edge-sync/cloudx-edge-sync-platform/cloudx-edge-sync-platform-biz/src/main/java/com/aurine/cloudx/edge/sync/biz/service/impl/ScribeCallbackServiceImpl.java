package com.aurine.cloudx.edge.sync.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.constant.KafkaConstant;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.open.api.inner.feign.RemotePushSubscribeService;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackModeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/01/27 11:50
 * @Package: com.aurine.cloudx.edge.sync.biz.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Service
public class ScribeCallbackServiceImpl implements ScribeCallbackService {

    @Resource
    private RemotePushSubscribeService remotePushSubscribeService;


    /**
     * 添加config订阅
     */
    @Override
    public R addConfigScribeCallback() {
        return remotePushSubscribeService.save(buildSubscribeCallbackRequest(null, KafkaConstant.TOPIC_CONFIG, OpenPushSubscribeCallbackTypeEnum.CONFIG.code));
    }

    @Override
    public List<OpenPushSubscribeCallbackVo> getScribeCallback(String projectUUID){
        R<List<OpenPushSubscribeCallbackVo>> list = remotePushSubscribeService.list(buildSubscribeCallbackRequest(projectUUID, null, null));
        if (list.getCode() != CommonConstants.SUCCESS) {
            return null;
        }
        return list.getData();
    }

    /**
     * 不同端代码不一致
     *
     * @param projectUUIDList
     */
    @Override
    public void addScribeCallback(String... projectUUIDList) {
        //向Open平台注册回调地址
        for (int i = 0; i < projectUUIDList.length; i++) {
            remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUIDList[i], KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.CASCADE.code));
            remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUIDList[i], KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.OPERATE.code));
            remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUIDList[i], KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.COMMAND.code));
            remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUIDList[i], KafkaConstant.TOPIC_EVENT, OpenPushSubscribeCallbackTypeEnum.EVENT.code));
            remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUIDList[i], KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.OTHER.code));
        }
    }

    @Override
    public void addCascadeScribeCallback(String projectUUID) {
        remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUID, KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.CASCADE.code));
    }

    @Override
    public void addScribeCallbackWithoutCascade(String projectUUID) {
        remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUID, KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.OPERATE.code));
        remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUID, KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.COMMAND.code));
        remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUID, KafkaConstant.TOPIC_EVENT, OpenPushSubscribeCallbackTypeEnum.EVENT.code));
        remotePushSubscribeService.save(buildSubscribeCallbackRequest(projectUUID, KafkaConstant.TOPIC_COMMON, OpenPushSubscribeCallbackTypeEnum.OTHER.code));
    }

    /**
     * 不同端代码不一致
     *
     * @param uuidList
     */
    @Override
    public void deleteScribeCallback(String... uuidList) {
        //向Open平台注册回调地址
        for (int i = 0; i < uuidList.length; i++) {
            remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.CASCADE.code, uuidList[i]);
            remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.OPERATE.code, uuidList[i]);
            remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.COMMAND.code, uuidList[i]);
            remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.EVENT.code, uuidList[i]);
            remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.OTHER.code, uuidList[i]);
        }
    }

    @Override
    public void deleteCascadeScribeCallback(String projectUUID) {
        remotePushSubscribeService.delete(Constants.appId, OpenPushSubscribeCallbackTypeEnum.CASCADE.code, projectUUID);
    }

    /**
     * 创建请求对象(MQ对象)
     *
     * @param projectUUID
     * @param callbackTopic
     * @param callbackType
     * @return
     */
    private OpenApiModel buildSubscribeCallbackRequest(String projectUUID, String callbackTopic, String callbackType) {
        OpenPushSubscribeCallbackVo subscribeCallback = new OpenPushSubscribeCallbackVo();
        subscribeCallback.setCallbackType(callbackType);
        subscribeCallback.setCallbackTopic(callbackTopic);
        subscribeCallback.setCallbackMode(OpenPushSubscribeCallbackModeEnum.TOPIC.code);

        if (projectUUID != null) {
            subscribeCallback.setProjectUUID(projectUUID);
        }
        subscribeCallback.setProjectType("1");

        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.parseObject(JSONObject.toJSONString(subscribeCallback)));
        openApiModel.setHeader(buildOpenApiHeader());
        log.info("openApiModel = {}", JSONObject.toJSONString(openApiModel));
        return openApiModel;
    }

    /**
     * 创建请求头
     *
     * @return
     */
    private OpenApiHeader buildOpenApiHeader() {
        OpenApiHeader openApiHeader = new OpenApiHeader();
        openApiHeader.setAppId(Constants.appId);
        return openApiHeader;
    }

}
