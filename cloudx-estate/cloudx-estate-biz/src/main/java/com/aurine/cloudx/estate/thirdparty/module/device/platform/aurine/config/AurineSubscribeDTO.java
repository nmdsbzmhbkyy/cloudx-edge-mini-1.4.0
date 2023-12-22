package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-27
 * @Copyright:
 */
@Data
public class AurineSubscribeDTO {

    /**
     * URL地址
     */
    private String url;

    /**
     * 订阅类型
     */
    private String type;

    /**
     * 是否订阅
     */
    private boolean enable;

    public boolean getEnable() {
        return this.enable;
    }

}
