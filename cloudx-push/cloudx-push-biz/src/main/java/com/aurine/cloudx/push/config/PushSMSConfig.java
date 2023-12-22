package com.aurine.cloudx.push.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * <p>SMS配置信息</p>
 * @ClassName: PushSMSConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 15:04
 * @Copyright:
 */
@Data
@Component
public class PushSMSConfig {

	@Value("${push.sms.url}")
	private String url;

	@Value("${push.sms.userid}")
	private String userid;

	@Value("${push.sms.password}")
	private String password;

	@Value("${push.sms.template.verification-code}")
	private String verificationCodeTemplate;




}
