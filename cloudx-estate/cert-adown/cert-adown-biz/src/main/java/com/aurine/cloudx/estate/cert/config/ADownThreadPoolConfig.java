package com.aurine.cloudx.estate.cert.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
/**
 * 下发线程池配置
 */
public class ADownThreadPoolConfig {

	/**
	 * 核心线程数（默认线程数）
	 */
	private static final int corePoolSize = 20;
	/**
	 * 最大线程数
	 */
	private static final int maxPoolSize = 100;
	/**
	 * 允许线程空闲时间（单位：默认为秒）
	 */
	private static final int keepAliveTime = 10;
	/**
	 * 缓冲队列大小
	 */
	private static final int queueCapacity = 200;
	/**
	 * 线程池名前缀
	 */
	private static final String threadNamePrefix = "Adown-Thread-";

	@Bean("adownExecutor")
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveTime);
		executor.setThreadNamePrefix(threadNamePrefix);

		// 线程池对拒绝任务的处理策略
		// CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 初始化
		executor.initialize();
		return executor;
	}

}
