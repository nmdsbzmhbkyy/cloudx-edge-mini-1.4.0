package com.aurine.cloudx.push.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @ClassName: AppConfig
 * @author: 邹宇
 * @date: 2021-8-26 09:29:15
 * @Copyright:
 */
@Component
@Data
@ToString
public class AppConfig {

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Value("${wx.server-token}")
    private String serverToken;
}
