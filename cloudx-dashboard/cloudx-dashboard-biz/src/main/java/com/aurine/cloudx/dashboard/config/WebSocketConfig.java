package com.aurine.cloudx.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-21
 * @Copyright:
 */
@Configuration
public class WebSocketConfig {

    /**
     *  注入对象ServerEndpointExporter，这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
