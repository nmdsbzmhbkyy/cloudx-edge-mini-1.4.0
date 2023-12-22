package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.push.machine.PushSendOperateMachine;
import org.springframework.stereotype.Component;

/**
 * 设备信息操作类型实现类
 *
 * @author : Qiu
 * @date : 2021 12 14 10:55
 */

@Component
public class PushSendOperateDeviceInfoMachineImpl implements PushSendOperateMachine {

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
    public String getServiceName() {
        return ServiceNameEnum.DEVICE_INFO.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
