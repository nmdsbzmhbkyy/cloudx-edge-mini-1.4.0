package com.aurine.cloudx.estate.cert.config;


import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * @description: 配置内容
 * @author: wangwei
 * @date: 2021/12/16 11:55
 **/
@Component
public class CertAdownConfig {
	@Resource
	private Environment env;


	/**
	 * 调度线程频率
	 */
	public static long dispatchThreadRate;
	/**
	 * 令牌生成频率（流量）
	 */
	public static long generateTokenRate;
	/**
	 * 下发线程操作频率
	 */
	public static long adownThreadRate;
	/**
	 * 监控线程频率
	 */
	public static long monitorThreadRate;
	/**
	 * 桶容量
	 */
	public static long tokenBucketSize;

	/**
	 * 队列入栈阈值，当队列容量小于阈值时，执行入栈操作
	 */
	public static long queueMinThresholdValue;
	/**
	 * 每次入栈的数据长度
	 */
	public static long queuePreSize;

	/**
	 * 下发超时时间
	 */
	public static long adownTimeoutValue;

	/**
	 * 下发超时时间 重试次数
	 */
	public static long adownTimeoutRetryTime;

	/**
	 * SQL缓存阈值
	 */
	public static long sqlCacheSize;
	/**
	 * SQL缓存提交频率
	 */
	public static long sqlCacheFlushRate;
	/**
	 * 下发线程池容量
	 */
	public static long adownThreadPoolSize;


	/**
	 * 遍历属性，并获取配置
	 *
	 * @throws Exception
	 */
	@PostConstruct
	public void readConfig() throws Exception {
		String prefix = "cert-adown.";
		Field[] fields = CertAdownConfig.class.getFields();
		for (Field field : fields) {
			field.set(0L, getProperty(prefix + field.getName()));
		}
	}

	/**
	 * 从配置文件获取属性
	 *
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Long getProperty(String key) throws UnsupportedEncodingException {
		return Long.valueOf(new String(env.getProperty(key).getBytes("ISO-8859-1"), "UTF-8"));
	}


	public static long getDispatchThreadRate() {
		return dispatchThreadRate;
	}


	public static long getGenerateTokenRate() {
		return generateTokenRate;
	}


	public static long getTokenBucketSize() {
		return tokenBucketSize;
	}


	public static long getAdownThreadRate() {
		return adownThreadRate;
	}


	public static long getQueueMinThresholdValue() {
		return queueMinThresholdValue;
	}

	public static long getQueuePreSize() {
		return queuePreSize;
	}

	public static long getAdownTimeoutValue() {
		return adownTimeoutValue;
	}

	public static long getMonitorThreadRate() {
		return monitorThreadRate;
	}

	public static long getAdownTimeoutRetryTime() {
		return adownTimeoutRetryTime;
	}

	public static long getSqlCacheSize() {
		return sqlCacheSize;
	}

	public static long getSqlCacheFlushRate() {
		return sqlCacheFlushRate;
	}

	public static long getAdownThreadPoolSize() {
		return adownThreadPoolSize;
	}

	public static void setAdownThreadPoolSize(long adownThreadPoolSize) {
		CertAdownConfig.adownThreadPoolSize = adownThreadPoolSize;
	}
}
