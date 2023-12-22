package com.aurine.cloudx.open.push.machine;

/**
 * 推送发送服务基础类
 *
 * @author : Qiu
 * @date : 2021 12 15 11:00
 */
public interface PushBaseMachine {

    /**
     * 获取服务名称
     *
     * @return
     */
    String getServiceName();

    /**
     * 获取版本号
     *
     * @return
     */
    String getVersion();
}
