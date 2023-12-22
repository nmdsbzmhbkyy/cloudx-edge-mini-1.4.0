package com.aurine.cloudx.estate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Auther: hjj
 * @Date: 2022/4/6 14:13
 * @Description:
 */
@Data
@Component
@RefreshScope
public class LiftCardConfig {
    @Value("${cloudx.rf.type}")
    private String type;
    @Value("${cloudx.rf.appId}")
    private String appId;
    @Value("${cloudx.rf.appSecret}")
    private String appSecret;
    @Value("${cloudx.rf.projectSecret}")
    private String projectSecret;
}
