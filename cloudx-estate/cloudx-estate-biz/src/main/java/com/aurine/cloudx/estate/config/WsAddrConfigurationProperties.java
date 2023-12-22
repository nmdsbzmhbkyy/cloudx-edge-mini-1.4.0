package com.aurine.cloudx.estate.config;

import com.aurine.cloudx.estate.entity.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wsaddress")
public class WsAddrConfigurationProperties {

    /**
     * 存支付相关的websocket地址
     */
    private String payWsAddr;

    private String notifyWsAddr;

}
