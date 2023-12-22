package com.aurine.cloudx.common.data.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 多租户配置
 *
 * @author oathsign
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cloudx.project")
public class ProjectConfigProperties {

	/**
	 * 维护租户列名称
	 */
	private String column = "projectId";

	/**
	 * 多租户的数据表集合
	 */
	private List<String> tables = new ArrayList<>();
}
