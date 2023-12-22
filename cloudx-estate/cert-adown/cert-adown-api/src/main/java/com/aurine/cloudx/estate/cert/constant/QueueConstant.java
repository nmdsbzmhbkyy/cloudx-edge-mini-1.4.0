package com.aurine.cloudx.estate.cert.constant;

/**
 * 下载缓存队列常量
 */
public class QueueConstant {


	/**
	 * 队列名称
	 */
	public static final String QUEUE_NAME = "CERT_CERT_ADOWN_QUEUE";


	/**
	 * 队列锁
	 */
	public static final String QUEUE_LOCK = "CERT_CERT_ADOWN_QUEUE_LOCK";

	/**
	 * 队列锁TTL
	 */
	public static final Integer QUEUE_LOCK_TTL = 3;

	/**
	 * 缓存队列长度
	 */
	public static final Integer QUERUE_SIZE = 10;


}
