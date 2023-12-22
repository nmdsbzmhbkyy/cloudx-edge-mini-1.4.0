package com.aurine.cloudx.push.feign;

import com.aurine.cloudx.common.core.constant.ServiceNameConstants;
import com.aurine.cloudx.push.dto.AppDTO;
import com.aurine.cloudx.push.dto.EmailDTO;
import com.aurine.cloudx.push.dto.SmsDTO;
import com.aurine.cloudx.push.dto.WxDTO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @description: 推送远程调用接口
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/30
 * @Copyright:
 */
@FeignClient(contextId = "remotePushService", value = ServiceNameConstants.PUSH_SERVICE)
public interface RemotePushService {

    /**
     * 单推到email
     *
     * @param emailDTO
     * @return
     */
    @PostMapping("/push/email")
    public R pushToEmail(EmailDTO emailDTO);

    /**
     * 群推到email
     *
     * @param emailDTO
     * @return
     */
    @PostMapping("/push/emails")
    public R pushToEmails(EmailDTO emailDTO);

    /**
     * 单推到sms
     *
     * @param smsDTO
     * @return
     */
    @PostMapping("/push/sms")
    public R pushToSMS(SmsDTO smsDTO);

    /**
     * 群推到sms
     *
     * @param smsDTO
     * @return
     */
    @PostMapping("/push/smss")
    public R pushToSMSs(SmsDTO smsDTO);

    /**
     * 单推信息给APP
     *
     * @param appDTO
     * @return
     */
    @PostMapping("/push/app")
    public R pushToApp(AppDTO appDTO);


    /**
     * 群推信息给APP
     *
     * @param appDTO
     * @return
     */
    @PostMapping("/push/apps")
    public R pushToApps(AppDTO appDTO);


    /**
     * 发送公众号消息
     *
     * @param wxDTO
     * @return
     */
    @PostMapping("/push/wx")
    public R pushToWx(WxDTO wxDTO);


}
