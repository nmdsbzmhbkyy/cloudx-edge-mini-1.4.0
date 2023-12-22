package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 级联入云业务类
 *
 * @author : Qiu
 * @date : 2021 12 27 9:33
 */
public interface PushSendCascadeMachine extends PushBaseMachine {

    /**
     * 申请
     *
     * @param model
     * @return
     */
    default OpenPushModel apply(OpenPushModel model) {
        return model;
    }

    /**
     * 撤销
     *
     * @param model
     * @return
     */
    default OpenPushModel revoke(OpenPushModel model) {
        return model;
    }

    /**
     * 同意
     *
     * @param model
     * @return
     */
    default OpenPushModel accept(OpenPushModel model) {
        return model;
    }

    /**
     * 拒绝
     *
     * @param model
     * @return
     */
    default OpenPushModel reject(OpenPushModel model) {
        return model;
    }

    /**
     * mqtt相关
     *
     * @param model
     * @return
     */
    default OpenPushModel mqtt(OpenPushModel model) {
        return model;
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    default OpenPushModel add(OpenPushModel model) {
        return model;
    }

    /**
     * 修改
     *
     * @param model
     * @return
     */
    default OpenPushModel update(OpenPushModel model) {
        return model;
    }

    /**
     * 删除
     *
     * @param model
     * @return
     */
    default OpenPushModel delete(OpenPushModel model) {
        return model;
    }
}
