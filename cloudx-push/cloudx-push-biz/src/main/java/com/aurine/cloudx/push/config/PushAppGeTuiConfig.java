package com.aurine.cloudx.push.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * <p>个推 APP 推送配置</p>
 * @ClassName: PushAppGTuiConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 17:05
 * @Copyright:
 */
@Data
@Component
@ConfigurationProperties(prefix = "push.app.gtui")
public class PushAppGeTuiConfig {

	/**
	 * 服务器连接
	 */
	private  String url;

	/**
	 * 配置列表
	 */
	private List<Map<String, String>> appList;


}
