package com.aurine.cloudx.edge.sync.common.utils;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.edge.sync.common.constant.sendConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class SendRetryUtil {


	/**
	 * 获取缓存数据
	 *
	 * @return
	 */
	public static Map<String, Long> getMap() {

		Map<Object, Object> retryMap = RedisUtil.hmget(sendConstant.SEND_RETRY_KEY);
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
		return RedisUtil.hset(sendConstant.SEND_RETRY_KEY, requestId, ++currTime);
	}


	/**
	 * 根据id获取数据
	 *
	 * @param requestId
	 * @return
	 */
	public static Long get(String requestId) {
		Object obj = RedisUtil.hget(sendConstant.SEND_RETRY_KEY, requestId);
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
		return RedisUtil.hSize(sendConstant.SEND_RETRY_KEY);
	}

	/**
	 * 从缓存中删除对象
	 *
	 * @param requestId
	 * @return
	 */
	public static void remove(String requestId) {
		RedisUtil.syncLock(sendConstant.SEND_RETRY_LOCK_KEY, sendConstant.SEND_CACHE_LOCK_TTL);

		RedisUtil.hdel(sendConstant.SEND_RETRY_KEY, requestId);

		RedisUtil.unLock(sendConstant.SEND_RETRY_LOCK_KEY);
	}


}
