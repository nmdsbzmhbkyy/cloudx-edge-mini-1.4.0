package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 推送发送服务操作类型接口
 *
 * @author : Qiu
 * @date : 2021 12 14 10:56
 */

public interface PushSendOperateMachine<T> extends PushBaseMachine {

    /**
     * 添加处理
     *
     * @param model
     * @return
     */
    default OpenPushModel add(OpenPushModel<T> model) {
        return model;
    }

    /**
     * 修改处理
     *
     * @param model
     * @return
     */
    default OpenPushModel update(OpenPushModel<T> model) {
        return model;
    }

    /**
     * 删除处理
     *
     * @param model
     * @return
     */
    default OpenPushModel delete(OpenPushModel<T> model) {
        return model;
    }

    /**
     * 状态处理
     *
     * @param model
     * @return
     */
    default OpenPushModel state(OpenPushModel<T> model) {
        return model;
    }

    /**
     * 同步处理
     * @param model
     * @return
     */
    default OpenPushModel sync(OpenPushModel<T> model){
        return model;
    }
}
