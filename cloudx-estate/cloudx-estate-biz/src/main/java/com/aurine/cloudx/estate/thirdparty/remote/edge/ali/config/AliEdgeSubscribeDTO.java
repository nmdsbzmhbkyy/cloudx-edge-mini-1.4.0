package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.config;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-10 9:52
 * @Copyright:
 */
@Data
public class AliEdgeSubscribeDTO {

    /**
     * URL回调地址
     */
    private String url;

    /**
     * 订阅类型
     * device.data
     * device.status
     * device
     * device.event
     *
     */
    private String resource;

    /**
     * 订阅事件
     * report
     * response
     * activate
     * update
     *
     */
    private String event;

    /**
     * 是否订阅
     */
    private boolean enable;

    public boolean getEnable() {
        return this.enable;
    }

}
