package com.aurine.cloudx.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * <p>极光 APP 推送配置</p>
 * @ClassName: PushAppGTuiConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 17:05
 * @Copyright:
 */
@Data
@Component
@ConfigurationProperties(prefix = "push.app.jiguang")
public class PushAppJiGuangConfig {
	/**
     * 配置列表
	 */
	private List<Map<String, String>> appList;


}
