package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.push.machine.PushSendCascadeMachine;
import org.springframework.stereotype.Component;

/**
 * 级联入云申请类型实现类
 *
 * @author : Qiu
 * @date : 2021 12 27 11:32
 */

@Component
public class PushSendCascadeApplyMachineImpl implements PushSendCascadeMachine {

    @Override
    public OpenPushModel apply(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel revoke(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel accept(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel reject(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel mqtt(OpenPushModel model) {
        return model;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.CASCADE_APPLY.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
