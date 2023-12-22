package com.aurine.cloudx.edge.sync.biz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author:zy
 * @data:2023/7/31 8:52 上午
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "skip")
public class SkipConfig {


    private List<String> serviceName;

    /**
     * 项目uuid
     */
    private List<String> projectUuid;


}
