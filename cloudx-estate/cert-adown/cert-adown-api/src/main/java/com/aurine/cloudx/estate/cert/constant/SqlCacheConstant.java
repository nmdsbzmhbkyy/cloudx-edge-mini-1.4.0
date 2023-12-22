package com.aurine.cloudx.estate.cert.constant;

/**
 *
 */
public class SqlCacheConstant {

	/**
	 * 刷新缓存频率
	 */
	public static final Integer FLUSH_RATE = 1;

	/**
	 * 更新SQL缓存
	 */
	public static final String UPDATE_SQL_CACHE_KEY = "CERT_UPDATE_SQL_CACHE";
	/**
	 *
	 */
	public static final String UPDATE_SQL_CACHE_KEY_LOCK = "CERT_UPDATE_SQL_CACHE_KEY_LOCK";
	public static final Long UPDATE_SQL_CACHE_KEY_LOCK_TTL = 3L;
	/**
	 * 新增SQL缓存
	 */
	public static final String INSERT_SQL_CACHE_KEY = "CERT_INSERT_SQL_CACHE";
	/**
	 *
	 */
	public static final String INSERT_SQL_CACHE_KEY_LOCK = "CERT_INSERT_SQL_CACHE_KEY_LOCK";
	public static final Long INSERT_SQL_CACHE_KEY_LOCK_TTL = 3L;

}
