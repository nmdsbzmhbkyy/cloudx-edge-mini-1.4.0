package com.aurine.cloudx.edge.sync.common.constant;

/**
 * 下发相关常量
 */
public class sendConstant {

	/**
	 * 下发时序 key
	 */
	public static final String SEND_SEQUENCE_KEY = "SEND_SEQUENCE_KEY";
	/**
	 * 下发时序 锁 key
	 */
	public static final String SEND_SEQUENCE_LOCK_KEY = "SEND_SEQUENCE_LOCK_KEY";

	public static final Long SEND_SEQUENCE_LOCK_TTL = 3L;
	/**
	 * 下发缓存 key
	 */
	public static final String SEND_CACHE_KEY = "SEND_CACHE_KEY";
	/**
	 * 下发缓存 lock key
	 */
	public static final String SEND_CACHE_LOCK_KEY = "SEND_CACHE_LOCK_KEY";

	public static final Long SEND_CACHE_LOCK_TTL = 3L;

	/**
	 * 重试次数 key
	 */
	public static final String SEND_RETRY_KEY = "SEND_RETRY_KEY";
	/**
	 * 重试次数 锁 key
	 */
	public static final String SEND_RETRY_LOCK_KEY = "SEND_RETRY_LOCK_KEY";

	public static final Long SEND_RETRY_LOCK_TTL = 3L;


}
