package com.aurine.cloudx.open.push.machine;

import com.aurine.cloudx.open.common.entity.model.OpenPushModel;

/**
 * 推送发送服务指令类型接口
 *
 * @author : Qiu
 * @date : 2021 12 14 10:56
 */

public interface PushSendCommandMachine extends PushBaseMachine {

    /**
     * 关闭处理
     *
     * @param model
     * @return
     */
    default OpenPushModel close(OpenPushModel model) {
        return model;
    }

    /**
     * 打开处理
     *
     * @param model
     * @return
     */
    default OpenPushModel open(OpenPushModel model) {
        return model;
    }

    /**
     * 改变处理
     *
     * @param model
     * @return
     */
    default OpenPushModel change(OpenPushModel model) {
        return model;
    }

    /**
     * 清空指令
     * @param model
     * @return
     */
    default OpenPushModel empty(OpenPushModel model){return model;}
}
