package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * @program: cloudx
 * @description: 腾讯配置类
 * @author: 谢泽毅
 * @create: 2021-08-11 11:57
 **/
@Component
@RefreshScope
public class TencentConfig {

    /**
     * 版本
     */
    @Value("${thirdparty.intercom.tencent.version}")
    public static String version;

    /**
     * url
     */
    @Value("${thirdparty.intercom.tencent.baseUrl}")
    public static String baseUrl;

}
