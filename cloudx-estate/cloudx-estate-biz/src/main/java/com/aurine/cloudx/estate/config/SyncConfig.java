package com.aurine.cloudx.estate.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author：zouyu
 * @data: 2022/3/22 11:10
 */
@Component
@Data
@ConfigurationProperties(prefix = "sync")
public class SyncConfig {

    /**
     * aurine库表名称
     */
    private List<String> serviceName;

    /**
     * aurine库同步表的数量
     */
    private Integer cloudxNum;

    /**
     * aurine_parking库表名称
     */
    private List<String> parkingServiceName;

    /**
     * aurine_parking库同步表的数量
     */
    private Integer parkingNum;

    /**
     * 每次同步的数量
     */
    private Integer number;

}
