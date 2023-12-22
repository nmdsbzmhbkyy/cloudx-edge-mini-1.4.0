package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 推送发送服务
 *
 * @author : Qiu
 * @date : 2021 12 10 9:30
 */

@FeignClient(contextId = "remotePushSendService", value = "cloudx-open-biz")
public interface RemotePushSendService {

    /**
     * 发送配置类消息
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/config")
    R config(@RequestBody OpenPushModel model);

    /**
     * 发送级联入云类消息
     * （申请apply、撤销revoke、同意accept、拒绝reject）
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/cascade")
    R cascade(@RequestBody OpenPushModel model);

    /**
     * 发送操作类消息
     * （添加add、修改update、删除delete）
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/operate")
    R operate(@RequestBody OpenPushModel model);

    /**
     * 发送指令类消息
     * （关闭close、打开open、改变change）
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/command")
    R command(@RequestBody OpenPushModel model);

    /**
     * 发送事件类消息
     * （人行、车行、告警）
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/event")
    R event(@RequestBody OpenPushModel model);

    /**
     * 发送其他消息
     *
     * @param model 推送模型对象
     * @return
     */
    @PostMapping("/v1/push/send/other")
    R other(@RequestBody OpenPushModel model);
}
