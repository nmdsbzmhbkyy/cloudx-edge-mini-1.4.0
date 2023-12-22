package com.aurine.cloudx.open.push.machine.impl;

import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.aurine.cloudx.open.push.machine.PushSendOperateMachine;
import org.springframework.stereotype.Component;

/**
 * 户型数据操作类型实现类
 *
 * @author : Qiu
 * @date : 2021 12 14 10:55
 */

@Component
public class PushSendOperateHouseDesignMachineImpl implements PushSendOperateMachine<HouseDesignVo> {

    @Override
    public OpenPushModel add(OpenPushModel<HouseDesignVo> model) {
//        OpenPushModel<HouseDesignVo> openPushModel = JSONObject.parseObject(JSONConvertUtils.objectToString(model), new TypeReference<OpenPushModel<HouseDesignVo>>() {
//        });
        return model;
    }

    @Override
    public OpenPushModel update(OpenPushModel<HouseDesignVo> model) {
        return model;
    }

    @Override
    public OpenPushModel delete(OpenPushModel<HouseDesignVo> model) {
        return model;
    }

    @Override
    public OpenPushModel state(OpenPushModel<HouseDesignVo> model) {
        return model;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.HOUSE_DESIGN.name;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
