package com.aurine.cloudx.estate.thirdparty.module.wr20.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WR20动态配置
 *
 * @ClassName: WR20ConfigProperties
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-10 9:01
 * @Copyright:
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "thirdparty.wr20")
public class WR20ConfigProperties {

    /**
     * 项目配置列表
     *     projectList:
     *       - projectid: test
     *         wr20id: 5ebb5524f2d7e4035a3327a7_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF
     *         enabled: true
     */
    private List<Map<String, String>> projectList = new ArrayList<>();
}
