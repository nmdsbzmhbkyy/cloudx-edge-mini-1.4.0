package com.aurine.cloudx.push.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor; 


/**
 * <p>线程池配置</p>
 * @ClassName: ThreadPoolConfig   
 * @author: wangwei<wangwei@aurine.cn>
 * @date: 2020年4月28日
 * @Copyright:
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(10);//5
		// 设置最大线程数
		executor.setMaxPoolSize(20);//10
		// 设置队列容量
		executor.setQueueCapacity(50);//20
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(600);
		// 设置默认线程名称
		executor.setThreadNamePrefix("Push-Thread-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

}
