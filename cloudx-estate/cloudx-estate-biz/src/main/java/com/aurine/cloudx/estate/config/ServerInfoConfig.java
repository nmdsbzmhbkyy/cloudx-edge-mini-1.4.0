package com.aurine.cloudx.estate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Auther: hjj
 * @Date: 2022/6/10 10:21
 * @Description:
 */
@Data
@Component
@RefreshScope
public class ServerInfoConfig {
    @Value("${server.local-mac}")
    private String mac;
}
