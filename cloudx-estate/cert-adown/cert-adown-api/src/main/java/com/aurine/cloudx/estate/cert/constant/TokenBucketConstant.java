package com.aurine.cloudx.estate.cert.constant;

/**
 * 令牌桶常量
 */
public class TokenBucketConstant {

	/**
	 * 默认令牌桶容量 容量为1时，令牌桶功能为对外限流，容量大于1时，功能为支撑突发并发量的对内限流
	 */
	public static final Integer TOKEN_BUCKET_DEFAULT_SIZE = 1;

	/**
	 * 令牌桶Redis Key前缀
	 */
	public static final String TOKEN_BUCKET_REDIS_PREFIX = "CERT_CERT_ADOWN_TOKEN_BUCKET_";
	/**
	 * 令牌桶Redis Key前缀
	 */
	public static final String TOKEN_BUCKET_REDIS = "CERT_CERT_ADOWN_TOKEN_BUCKET";

	/**
	 * 令牌桶存在时间
	 */
	public static final Integer TOKEN_BUCKET_REDIS_TTL = 30;

	/**
	 * 令牌桶同步锁
	 */
	public static final String TOKEN_BUCKET_REDIS_LOCK_PREFIX = "CERT_LOCK_";

	/**
	 * 令牌桶同步锁过期时间
	 */
	public static final Integer TOKEN_BUCKET_REDIS_LOCK_TTL = 3;


}
