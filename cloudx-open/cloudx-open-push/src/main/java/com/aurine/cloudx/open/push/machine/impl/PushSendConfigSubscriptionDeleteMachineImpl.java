package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.push.machine.PushSendConfigMachine;
import org.springframework.stereotype.Component;

/**
 * 配置订阅删除实现类
 *
 * @author : Qiu
 * @date : 2021 12 27 11:32
 */

@Component
public class PushSendConfigSubscriptionDeleteMachineImpl implements PushSendConfigMachine {

    @Override
    public OpenPushModel doConfig(OpenPushModel model) {
        return model;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.CONFIG_SUBSCRIPTION_DELETE.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
