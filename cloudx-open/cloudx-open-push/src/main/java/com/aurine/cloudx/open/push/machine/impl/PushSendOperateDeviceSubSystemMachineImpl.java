package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.push.machine.PushSendOperateMachine;
import org.springframework.stereotype.Component;

@Component
public class PushSendOperateDeviceSubSystemMachineImpl implements PushSendOperateMachine {
    @Override
    public OpenPushModel add(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel update(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel delete(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel state(OpenPushModel model) {
        return model;
    }

    @Override
    public OpenPushModel sync(OpenPushModel model) {
        return model;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.DEVICE_SUBSYSTEM.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
