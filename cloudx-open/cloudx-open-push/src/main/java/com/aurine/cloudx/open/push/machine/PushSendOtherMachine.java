package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 推送发送服务其他类型接口
 *
 * @author : Qiu
 * @date : 2021 12 14 10:56
 */

public interface PushSendOtherMachine extends PushBaseMachine {

    /**
     * 其他处理
     * @param model
     * @return
     */
    default OpenPushModel other(OpenPushModel model) {
        return model;
    }
}
