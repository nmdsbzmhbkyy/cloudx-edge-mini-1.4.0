package com.aurine.cloudx.open.push.handler;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 推送模块发送处理接口
 *
 * @author : Qiu
 * @date : 2021 12 13 15:23
 */

public interface PushSendHandler {

    /**
     * 处理方法
     *
     * @param model
     * @return
     */
    R handle(OpenPushModel model);

    /**
     * 获取服务类型
     *
     * @return
     */
    String getServiceType();

    /**
     * 获取版本号
     *
     * @return
     */
    String getVersion();
}
