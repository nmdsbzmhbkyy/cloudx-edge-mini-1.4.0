package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * @description: 下发重试工具类
 * @author: wangwei
 * @date: 2021/12/24 15:27
 **/
@Slf4j
public class AdownRetryUtil {


	/**
	 * 获取缓存数据
	 *
	 * @return
	 */
	public static Map<String, Long> getMap() {

		Map<Object, Object> retryMap = CertRedisUtil.hmget(AdownConstant.ADOWN_RETRY_KEY);
		Map<String, Long> resultMap = new HashMap<>();

		if (CollUtil.isNotEmpty(retryMap)) {
			for (Object key : retryMap.keySet()) {
				resultMap.put(key.toString(), (Long) retryMap.get(key));
			}
		}

		return resultMap;
	}

	/**
	 * 重试值+1
	 *
	 * @param requestId
	 * @return 返回重试次数
	 */
	public static boolean add(String requestId) {

		Long currTime = get(requestId);
		return CertRedisUtil.hset(AdownConstant.ADOWN_RETRY_KEY, requestId, ++currTime);
	}


	/**
	 * 根据id获取数据
	 *
	 * @param requestId
	 * @return
	 */
	public static Long get(String requestId) {
		Object obj = CertRedisUtil.hget(AdownConstant.ADOWN_RETRY_KEY, requestId);
		Long result = 0L;

		if (obj != null) {
			result = (Long) obj;
		}

		return result;
	}


	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long size() {
		return CertRedisUtil.hSize(AdownConstant.ADOWN_RETRY_KEY);
	}

	/**
	 * 从缓存中删除对象
	 *
	 * @param requestId
	 * @return
	 */
	public static void remove(String requestId) {
		CertRedisUtil.syncLock(AdownConstant.ADOWN_RETRY_LOCK_KEY, AdownConstant.ADOWN_RETRY_LOCK_TTL);

		CertRedisUtil.hdel(AdownConstant.ADOWN_RETRY_KEY, requestId);

		CertRedisUtil.unLock(AdownConstant.ADOWN_RETRY_LOCK_KEY);
	}


}
