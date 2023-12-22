package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.push.machine.PushSendEventMachine;
import org.springframework.stereotype.Component;

/**
 * 人行事件类型实现类
 *
 * @author : Qiu
 * @date : 2021 12 15 11:32
 */

@Component
public class PushSendEventPersonEntranceMachineImpl implements PushSendEventMachine {

    @Override
    public OpenPushModel event(OpenPushModel model) {
        return model;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_ENTRANCE.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
