package com.aurine.cloudx.push.service.impl;

import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.push.config.PushMailConfig;
import com.aurine.cloudx.push.config.PushSMSConfig;
import com.aurine.cloudx.push.entity.PushMessage;
import com.aurine.cloudx.push.handler.AppHandlerStrategyFactory;
import com.aurine.cloudx.push.service.SendMessageService;
import com.aurine.cloudx.push.util.PushWxUtil;
import com.cloudx.common.push.util.PushMailUtil;
import com.cloudx.common.push.util.PushSMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 推送服务实现 </p>
 *
 * @ClassName: PushNoticeServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/28 9:20
 * @Copyright:
 */
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    private PushSMSConfig pushSMSConfig;
    @Autowired
    private PushMailConfig pushMailConfig;
    @Autowired
    private AppHandlerStrategyFactory appHandlerStrategyFactory;


    /**
     * 单推APP
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToApp(PushMessage pushMessage) {
        // 根据推送服务选择实现类
        return appHandlerStrategyFactory.sendMsgToApp(pushMessage);
    }

    /**
     * 群推APP
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToApps(PushMessage pushMessage) {
        return appHandlerStrategyFactory.batchSendMsgToApps(pushMessage);
    }

    /**
     * 单推Email
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToEmail(PushMessage pushMessage) {
        String type = MapUtil.getStr(pushMessage.getParamMap(), "type");
        if (type == null) type = "text";


        if (type.equalsIgnoreCase("html")) {
            //发送html格式邮件
            return PushMailUtil.sendHtmlMail(pushMessage.getTargetId(), pushMessage.getTitle(), pushMessage.getMessage(),
                    pushMailConfig.getHost(), pushMailConfig.getUsername(), pushMailConfig.getPassword(),
                    pushMailConfig.getFrom(), pushMailConfig.getFromName());

        } else {
            return PushMailUtil.sendSimpleEmail(pushMessage.getTargetId(), pushMessage.getTitle(), pushMessage.getMessage(),
                    pushMailConfig.getHost(), pushMailConfig.getUsername(), pushMailConfig.getPassword(),
                    pushMailConfig.getFrom(), pushMailConfig.getFromName());
        }

    }

    /**
     * 群推Email
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToEmails(PushMessage pushMessage) {

        String type = MapUtil.getStr(pushMessage.getParamMap(), "type");
        if (type == null) type = "text";
        boolean result = false;

        for (String targetId : pushMessage.getTargetIdList()) {
            if (type.equalsIgnoreCase("html")) {
                //发送html格式邮件
                result = PushMailUtil.sendHtmlMail(targetId, pushMessage.getTitle(), pushMessage.getMessage(),
                        pushMailConfig.getHost(), pushMailConfig.getUsername(), pushMailConfig.getPassword(),
                        pushMailConfig.getFrom(), pushMailConfig.getFromName());
            } else {
                result = PushMailUtil.sendSimpleEmail(targetId, pushMessage.getTitle(), pushMessage.getMessage(),
                        pushMailConfig.getHost(), pushMailConfig.getUsername(), pushMailConfig.getPassword(),
                        pushMailConfig.getFrom(), pushMailConfig.getFromName());
            }
        }
        return result;

    }

    /**
     * 单推短信
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToSMS(PushMessage pushMessage) throws Exception {
        return PushSMSUtil.sendSmsMessage(pushMessage.getTargetId(), pushMessage.getMessage(), pushSMSConfig.getUrl(), pushSMSConfig.getUserid(), pushSMSConfig.getPassword());
    }

    /**
     * 群发短信
     *
     * @param pushMessage
     */
    @Override
    public boolean sendToSMSs(PushMessage pushMessage) throws Exception {
        boolean result = false;
        for (String targetId : pushMessage.getTargetIdList()) {
            result = PushSMSUtil.sendSmsMessage(targetId, pushMessage.getMessage(), pushSMSConfig.getUrl(), pushSMSConfig.getUserid(), pushSMSConfig.getPassword());
        }
        return result;
    }

    @Override
    public boolean sendToWx(PushMessage pushMessage) throws Exception {
        return PushWxUtil.sendWxMessage(pushMessage.getTemplateId(),pushMessage.getUnionId(),pushMessage.getData());
    }
}
