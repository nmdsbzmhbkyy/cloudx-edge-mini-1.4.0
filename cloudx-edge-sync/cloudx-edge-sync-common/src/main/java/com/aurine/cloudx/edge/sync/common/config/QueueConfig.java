package com.aurine.cloudx.edge.sync.common.config;


/**
 * @description: 配置内容
 * @author: wangwei
 * @date: 2021/12/16 11:55
 **/
public class QueueConfig {

	/**
	 * 调度线程频率
	 */
	private static long dispatchThreadRate = 5000L;
	/**
	 * 令牌生成频率（流量）
	 */
	private static long generateTokenRate = 10000L;
	/**
	 * 下发线程操作频率
	 */
	private static long adownThreadRate = 5000L;
	/**
	 * 监控线程频率
	 */
	private static long monitorThreadRate = 500L;
	/**
	 * 桶容量
	 */
	private static long tokenBucketSize = 10L;

	/**
	 * 队列入栈阈值，当队列容量小于阈值时，执行入栈操作
	 */
	private static long queueMinThresholdValue = 1L;
	/**
	 * 每次入栈的数据长度
	 */
	private static long queuePreSize = 10L;

	/**
	 * 下发超时时间
	 */
	private static long adownTimeoutValue = 10L;

	/**
	 * 下发超时时间 重试次数
	 */
	private static long adownTimeoutRetryTime = 3L;


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

}
