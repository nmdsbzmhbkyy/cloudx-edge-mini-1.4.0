package com.aurine.cloudx.push.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * <p>EMail配置信息</p>
 * @ClassName: PushMailConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 15:04
 * @Copyright:
 */
@Data
@Component
public class PushMailConfig {

	@Value("${push.email.host}")
	private String host;
	@Value("${push.email.username}")
	private String username;
	@Value("${push.email.password}")
	private String password;
	@Value("${push.email.from}")
	private String from;
	@Value("${push.email.from-name}")
	private String fromName;
	@Value("${push.email.title}")
	private String title;


}
