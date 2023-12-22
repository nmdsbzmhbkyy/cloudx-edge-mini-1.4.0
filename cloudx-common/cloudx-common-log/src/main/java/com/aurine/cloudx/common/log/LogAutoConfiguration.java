package com.aurine.cloudx.common.log;

import com.aurine.cloudx.estate.feign.RemoteOperationLogService;
import com.aurine.cloudx.common.log.aspect.ProjSysLogAspect;
import com.aurine.cloudx.common.log.event.ProjSysLogListener;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lengleng
 * @date 2018/6/28
 * <p>
 * 日志自动配置
 */
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {
	private final RemoteOperationLogService remoteLogService;

	@Bean
	public ProjSysLogListener projSysLogListener() {
		return new ProjSysLogListener(remoteLogService);
	}

	@Bean
	public ProjSysLogAspect projSysLogAspect(ApplicationEventPublisher publisher) {
		return new ProjSysLogAspect(publisher);
	}
}
