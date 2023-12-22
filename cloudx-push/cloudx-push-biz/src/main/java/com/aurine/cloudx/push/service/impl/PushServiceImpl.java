package com.aurine.cloudx.push.service.impl;

import cn.hutool.core.lang.Validator;
import com.aurine.cloudx.push.constant.EmailTypeEnum;
import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.aurine.cloudx.push.constant.PushTypeEnum;
import com.aurine.cloudx.push.entity.PushMessage;
import com.aurine.cloudx.push.service.PushService;
import com.aurine.cloudx.push.stream.PushStreams;
import com.cloudx.common.push.constant.OSTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 推送服务实现 </p>
 *
 * @ClassName: PushNoticeServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/28 9:20
 * @Copyright:
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    @Autowired
    private final PushStreams pushStreams;


    public PushServiceImpl(PushStreams pushStreams) {
        this.pushStreams = pushStreams;
    }

    @Override
    public R pushToEmail(String email, String title, String message, EmailTypeEnum type) {
        if (!Validator.isEmail(email)) return R.failed(email + " 不是有效的Email地址");
        return this.pushToKafka(new PushMessage(PushTypeEnum.EMAIL, email, title, message, getEmailParam(type)), PushTypeEnum.EMAIL);
    }

    /**
     * 群推到email
     *
     * @param emailList
     * @param title
     * @param message
     * @return
     */
    @Override
    public R pushToEmails(List<String> emailList, String title, String message, EmailTypeEnum type) {
        for (String email : emailList) {
            if (!Validator.isEmail(email)) return R.failed(email + " 不是有效的Email地址");
        }
        return this.pushToKafka(new PushMessage(PushTypeEnum.EMAILS, emailList, title, message, getEmailParam(type)), PushTypeEnum.EMAILS);
    }

    /**
     * 拼装email参数
     *
     * @param type
     * @return
     */
    private Map<String, Object> getEmailParam(EmailTypeEnum type) {
        Map<String, Object> paramMap = new HashMap<>();
        switch (type) {
            case html:
                paramMap.put("type", "html");
                break;
            case text:
            default:
                paramMap.put("type", "text");
        }


        return paramMap;
    }

    /**
     * 单推到sms
     *
     * @param mobile
     * @param message
     * @return
     */
    @Override
    public R pushToSMS(String mobile, String message) {
        if (!Validator.isMobile(mobile)) return R.failed(mobile + " 不是有效的电话号码");
        return this.pushToKafka(new PushMessage(PushTypeEnum.SMS, mobile, null, message, null), PushTypeEnum.SMS);
    }

    /**
     * 群推到sms
     *
     * @param mobileList
     * @param message
     * @return
     */
    @Override
    public R pushToSMSs(List<String> mobileList, String message) {
        for (String mobile : mobileList) {
            if (!Validator.isMobile(mobile)) return R.failed(mobile + " 不是有效的电话号码");
        }
        return this.pushToKafka(new PushMessage(PushTypeEnum.SMSS, mobileList, null, message, null), PushTypeEnum.SMSS);
    }

    /**
     * 单推信息给APP
     *
     * @param clientId   客户端ID
     * @param osType     客户端操作系统
     * @param message    推送的信息
     * @param systemType 推送系统类型
     * @param paramMap   配置参数
     * @return
     */
    @Override
    public R pushToApp(String clientId, OSTypeEnum osType, String message, PushSystemEnum systemType, String appName, Map<String, Object> paramMap) {
        return this.pushToKafka(new PushMessage(PushTypeEnum.APP, clientId, message, systemType, appName, osType, paramMap), PushTypeEnum.APP);
    }

    /**
     * 群推信息给APP
     *
     * @param clientIdList 客户端列表
     * @param osTypeList   客户端类型列表，排序需要与客户端保持一致
     * @param message
     * @param systemType
     * @param paramMap     配置参数
     * @return
     */
    @Override
    public R pushToApps(List<String> clientIdList, List<OSTypeEnum> osTypeList, String message, PushSystemEnum systemType, String appName, Map<String, Object> paramMap) {
        return this.pushToKafka(new PushMessage(PushTypeEnum.APPS, clientIdList, message, systemType, appName, osTypeList, paramMap), PushTypeEnum.APPS);
    }

    /**
     * 发送公众号消息
     *
     * @param templateId 模板Id
     * @param unionId 用户unionId
     * @param data  模板数据
     * @return
     */
    @Override
    public R pushToWx(String templateId ,List<String> unionId, Map<String, Object> data) {
        return this.pushToKafka(new PushMessage(PushTypeEnum.Wx,unionId,data,templateId),PushTypeEnum.Wx);
    }

    /**
     * 推送消息
     *
     * @param pushNotice
     * @return
     */
    private R pushToKafka(PushMessage pushNotice, PushTypeEnum pushTypeEnum) {
        MessageChannel messageChannel = null;

        switch (pushTypeEnum) {
            case SMS:
                messageChannel = pushStreams.outboundSms();
                break;
            case SMSS:
                messageChannel = pushStreams.outboundSmss();
                break;
            case EMAIL:
                messageChannel = pushStreams.outboundEmail();
                break;
            case EMAILS:
                messageChannel = pushStreams.outboundEmails();
                break;
            case APP:
                messageChannel = pushStreams.outboundApp();
                break;
            case APPS:
                messageChannel = pushStreams.outboundApps();
                break;
            case Wx:
                messageChannel = pushStreams.outboundWx();
                break;
            default:
                return R.failed(pushTypeEnum.name() + "：该推送方式未实现");
        }

        log.info("Sending pushNotice To Streams {}", pushNotice);

        messageChannel.send(MessageBuilder
                .withPayload(pushNotice)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());

        return R.ok();
    }


}
