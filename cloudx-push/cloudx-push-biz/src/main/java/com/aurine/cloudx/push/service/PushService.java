package com.aurine.cloudx.push.service;

import com.aurine.cloudx.push.template.TemplateData;
import com.aurine.cloudx.push.constant.EmailTypeEnum;
import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.cloudx.common.push.constant.OSTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;
import java.util.Map;

/**
 * @description: 推送接口
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */
public interface PushService {

    /**
     * 单推到email
     *
     * @param email
     * @param title
     * @param message
     * @param type      text/html
     * @return
     */
    public R pushToEmail(String email, String title, String message, EmailTypeEnum type);

    /**
     * 群推到email
     *
     * @param emailList
     * @param title
     * @param message
     * @return
     */
    public R pushToEmails(List<String> emailList, String title, String message, EmailTypeEnum type);

    /**
     * 单推到sms
     *
     * @param mobile
     * @param message
     * @return
     */
    public R pushToSMS(String mobile, String message);

    /**
     * 群推到sms
     *
     * @param mobileList
     * @param message
     * @return
     */
    public R pushToSMSs(List<String> mobileList, String message);

    /**
     * 单推信息给APP
     *
     * @param clientId   客户端ID
     * @param osType     客户端操作系统
     * @param message    推送的信息
     * @param systemType 推送系统类型，个推、极光、华为
     * @param appName      要推送的APP
     * @param paramMap   配置参数
     * @return
     */
    public R pushToApp(String clientId, OSTypeEnum osType, String message, PushSystemEnum systemType, String appName, Map<String, Object> paramMap);


    /**
     * 群推信息给APP
     *
     * @param clientIdList 客户端列表
     * @param osTypeList   客户端类型列表，排序需要与客户端保持一致
     * @param message
     * @param systemType  推送系统类型，个推、极光、华为
     * @param appName      要推送的APP
     * @param paramMap   配置参数
     * @return
     */
    public R pushToApps(List<String> clientIdList, List<OSTypeEnum> osTypeList, String message, PushSystemEnum systemType, String appName, Map<String, Object> paramMap);


    /**
     * 发送公众号消息
     *
     * @param templateId 模板Id
     * @param unionId 用户unionId
     * @param data  模板数据
     * @return
     */
    public R pushToWx(String templateId ,List<String> unionId, Map<String, Object> data);
}
