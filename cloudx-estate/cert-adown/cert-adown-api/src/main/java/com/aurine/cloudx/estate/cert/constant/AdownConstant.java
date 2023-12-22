package com.aurine.cloudx.estate.cert.constant;

/**
 * 下发相关常量
 */
public class AdownConstant {

	/**
	 * 下发时序 key
	 */
	public static final String ADOWN_SEQUENCE_KEY = "CERT_ADOWN_SEQUENCE_KEY";
	/**
	 * 下发时序 锁 key
	 */
	public static final String ADOWN_SEQUENCE_LOCK_KEY = "CERT_ADOWN_SEQUENCE_LOCK_KEY";

	public static final Long ADOWN_SEQUENCE_LOCK_TTL = 3L;
	/**
	 * 下发缓存 key
	 */
	public static final String ADOWN_CACHE_KEY = "CERT_ADOWN_CACHE_KEY";
	/**
	 * 下发缓存 lock key
	 */
	public static final String ADOWN_CACHE_LOCK_KEY = "CERT_ADOWN_CACHE_LOCK_KEY";

	public static final Long ADOWN_CACHE_LOCK_TTL = 3L;

	/**
	 * 重试次数 key
	 */
	public static final String ADOWN_RETRY_KEY = "CERT_ADOWN_RETRY_KEY";
	/**
	 * 重试次数 锁 key
	 */
	public static final String ADOWN_RETRY_LOCK_KEY = "cert-adown:CERT_ADOWN_RETRY_LOCK_KEY";

	public static final Long ADOWN_RETRY_LOCK_TTL = 3L;

	/**
	 * 正在下发凭证设备缓存KEY前缀，用于凭证下发超时判断和正在下发判断
	 * */
	public static final String DEVICE_CERT_ADOWN_PROCESSING_PREFIX = "cert-adown:PROCESSING_DEVICE:";


	/**设备凭证下发调度队列KEY*/
	public static final String DEVICE_DISPATCH_QUEUE_KEY = "cert-adown:DEVICE_DISPATCH_QUEUE";

	/**
	 * 凭证下发设备锁前缀
	 * */
	public static final String CERT_ADOWN_DISPATCH_TASK_LOCK = "cert-adown:DISPATCH_TASK_LOCK:";
	/**
	 * 凭证下发设备锁前缀
	 * */
	public static final String CERT_ADOWN_DEVICE_LOCK_PREFIX = "cert-adown:DEVICE_LOCK:";
	/**
	 * 凭证下发处理数据缓存前缀
	 * */
	public static final String CERT_ADOWN_PROCESSING_REQUEST_PREFIX = "cert-adown:PROCESSING_REQUEST:";
	/**
	 * 凭证下发设备超时检查锁KEY
	 * */
	public static final String CERT_ADOWN_TIMEOUT_CHECK_LOCK = "cert-adown:TIMEOUT_CHECK_LOCK";
}
