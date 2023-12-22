package com.aurine.cloudx.push.service;

import com.aurine.cloudx.push.entity.PushMessage;

/**
 * <p>消息推送服务接口</p>
 *
 * @ClassName: pushMessageService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/28 9:08
 * @Copyright:
 */
public interface SendMessageService {


    /**
     * 单推APP
     *
     * @param pushMessage
     */
    public boolean sendToApp(PushMessage pushMessage);

    /**
     * 群推APP
     *
     * @param pushMessage
     */
    public boolean sendToApps(PushMessage pushMessage);

    /**
     * 单推Email
     *
     * @param pushMessage
     */
    public boolean sendToEmail(PushMessage pushMessage);

    /**
     * 群推Email
     *
     * @param pushMessage
     */
    public boolean sendToEmails(PushMessage pushMessage);

    /**
     * 单推短信
     *
     * @param pushMessage
     */
    public boolean sendToSMS(PushMessage pushMessage) throws Exception;

    /**
     * 群发短信
     *
     * @param pushMessage
     */
    public boolean sendToSMSs(PushMessage pushMessage) throws Exception;

    /**
     * 公众号发送消息
     *
     * @param pushMessage
     */
    public boolean sendToWx(PushMessage pushMessage) throws Exception;

}
