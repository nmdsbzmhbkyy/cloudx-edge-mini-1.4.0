package com.aurine.cloudx.estate.thirdparty.transport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 传输配置
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-16
 * @Copyright:
 */
@Component
public class TransportConfig {

    /**
     * 连接超时长度 秒
     */
    @Value("${transport.timeout:5}")
    public  Long timeout;
}
