package com.aurine.cloudx.wjy.canal.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @ClassName: CanalConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 8:37
 * @Copyright:
 */
@Data
@Component
public class CanalConfig {

    @Value("${canal.server.host}")
    private String host;

    @Value("${canal.server.port}")
    private int port;

    @Value("${canal.server.destination}")
    private String destination;

    @Value("${canal.server.username}")
    private String username;

    @Value("${canal.server.password}")
    private String password;

    /**
     * 单次处理信息数量
     */
    @Value("${canal.server.batch-size}")
    private Integer batchSize;

    /**
     * 订阅正则表达式
     */
    @Value("${canal.server.subscribe-regex}")
    private String subscribeRegex;
}
