package com.aurine.cloudx.estate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@RefreshScope
@Configuration
@Data
public class AliyunVcsConfigure {
    @Value("${aliyun.vsc.accessKeyId:}")
    private String accessKeyId;
    
    @Value("${aliyun.vsc.accessSecret:}")
    private String accessSecret;
}
