package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 级联入云配置类
 *
 * @author : Qiu
 * @date : 2022 02 11 11:33
 */
public interface PushSendConfigMachine extends PushBaseMachine {

    /**
     * 执行配置
     *
     * @param model
     * @return
     */
    default OpenPushModel doConfig(OpenPushModel model) {
        return model;
    }
}
