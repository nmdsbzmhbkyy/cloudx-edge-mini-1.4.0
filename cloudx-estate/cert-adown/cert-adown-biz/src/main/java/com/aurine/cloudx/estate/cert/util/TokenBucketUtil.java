package com.aurine.cloudx.estate.cert.util;


import com.aurine.cloudx.estate.cert.config.CertAdownConfig;
import com.aurine.cloudx.estate.cert.constant.TokenBucketConstant;
import lombok.extern.slf4j.Slf4j;


/**
 * 令牌桶工具类
 *
 * @description:
 * @author: wangwei
 * @date: 2021/12/17 10:42
 **/
@Slf4j
public class TokenBucketUtil {

	public static long size() {
		return CertRedisUtil.lGetListSize(TokenBucketConstant.TOKEN_BUCKET_REDIS);
	}

	/**
	 * 生成令牌，并放入桶中
	 * 如超出桶容量则抛弃该令牌
	 */
	public static void generateToken() {
		generateToken(TokenBucketConstant.TOKEN_BUCKET_REDIS);
	}

	/**
	 * 生成令牌，并放入桶中
	 * 如超出桶容量则抛弃该令牌
	 *
	 * @param bucketName
	 */
	public static void generateToken(String bucketName) {
		//桶不存在，创建桶，并倒入令牌
		if (!CertRedisUtil.hasKey(bucketName)) {
			CertRedisUtil.lSet(bucketName, UUID.randomUUID(), TokenBucketConstant.TOKEN_BUCKET_REDIS_TTL);
		} else {
			//桶续租
			CertRedisUtil.expire(bucketName, TokenBucketConstant.TOKEN_BUCKET_REDIS_TTL);

			//桶已存在，检测桶是否已满，未满则倒入令牌
			if (CertRedisUtil.lGetListSize(bucketName) <= CertAdownConfig.getTokenBucketSize() - 1) {
				CertRedisUtil.lSet(bucketName, UUID.randomUUID(), TokenBucketConstant.TOKEN_BUCKET_REDIS_TTL);
			}

		}
		log.debug("当前桶有{}个token", CertRedisUtil.lGetListSize(bucketName));
	}


	/**
	 * 从桶中获取令牌，有令牌返回true，没有返回false,线程安全
	 *
	 * @return
	 */
	public static boolean getToken() {
		return getToken(TokenBucketConstant.TOKEN_BUCKET_REDIS);
	}

	/**
	 * 从桶中获取令牌，有令牌返回true，没有返回false,线程安全
	 *
	 * @param bucketName
	 * @return
	 */
//	public static boolean getToken(String bucketName) {
//		CertRedisUtil.syncLock(TokenBucketConstant.TOKEN_BUCKET_REDIS_LOCK_PREFIX + bucketName, TokenBucketConstant.TOKEN_BUCKET_REDIS_LOCK_TTL);
//		boolean result = false;
//		if (!CertRedisUtil.hasKey(bucketName)) {
//			result = false;
//		} else {
//			//桶已存在, 且桶内有令牌，获取一块令牌
//			if (CertRedisUtil.lGetListSize(bucketName) >= 1) {
//				Object token = CertRedisUtil.lGetIndex(bucketName, 0);
//				log.info("[CERT-ADOWN] 获取到Token {}",token);
//				CertRedisUtil.lRemove(bucketName, 1, token);
//				result = true;
//			}
//		}
//
//		CertRedisUtil.unLock(TokenBucketConstant.TOKEN_BUCKET_REDIS_LOCK_PREFIX + bucketName);
//		return result;
//	}
	/**
	 * 从桶中获取令牌，有令牌返回true，没有返回false,使用redis原子性操作实现
	 *
	 * @param bucketName
	 * @return
	 */
	public static boolean getToken(String bucketName) {
		boolean result = false;

		if (!CertRedisUtil.hasKey(bucketName)) {
			result = false;
		} else {
			// 桶已存在且桶内有令牌，获取一块令牌
			Object token = CertRedisUtil.lPop(bucketName);
			if (token != null) {
				log.info("[CERT-ADOWN] 获取到Token {}", token);
				result = true;
			}
		}

		return result;
	}
}
