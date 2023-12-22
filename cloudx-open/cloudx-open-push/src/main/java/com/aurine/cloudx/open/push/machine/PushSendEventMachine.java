package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 推送发送服务事件类型接口
 *
 * @author : Qiu
 * @date : 2021 12 14 10:56
 */

public interface PushSendEventMachine extends PushBaseMachine {

    /**
     * 事件处理
     *
     * @param model
     * @return
     */
    default OpenPushModel event(OpenPushModel model) {
        return model;
    }
}
