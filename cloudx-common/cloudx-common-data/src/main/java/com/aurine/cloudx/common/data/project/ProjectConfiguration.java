package com.aurine.cloudx.common.data.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

/**
 * feign 项目信息拦截
 * @ClassName:  ProjectConfiguration   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年5月7日 上午10:54:30      
 * @Copyright:
 */
@Configuration
@ComponentScan("com.aurine.cloudx.common.data.project")
public class ProjectConfiguration {
	@Bean
	public RequestInterceptor projectFeignInterceptor() {
		return new ProjectFeignInterceptor();
	}
}
