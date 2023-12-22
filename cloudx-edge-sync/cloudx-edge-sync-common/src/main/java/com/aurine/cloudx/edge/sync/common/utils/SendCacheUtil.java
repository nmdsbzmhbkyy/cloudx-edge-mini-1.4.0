package com.aurine.cloudx.edge.sync.common.utils;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.constant.sendConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class SendCacheUtil {

	/**
	 * 获取缓存数据
	 *
	 * @return
	 */
	public static List<TaskInfo> list() {

		Map<Object, Object> cacheMap = RedisUtil.hmget(sendConstant.SEND_CACHE_KEY);
		List<TaskInfo> list = new ArrayList<>();

		if (CollUtil.isNotEmpty(cacheMap)) {
			for (Object obj : cacheMap.values()) {
				list.add((TaskInfo) obj);
			}
		}


		return list;
	}


	/**
	 * 根据id获取数据
	 *
	 * @param requestId
	 * @return
	 */
	public static TaskInfo get(String requestId) {
		Object obj = RedisUtil.hget(sendConstant.SEND_CACHE_KEY, requestId);
		TaskInfo result = null;

		if (obj != null) {
//			RedisUtil.hdel(sendConstant.SEND_CACHE_KEY, obj);
			result = (TaskInfo) obj;
		}

		return result;
	}


	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long size() {
		return RedisUtil.hSize(sendConstant.SEND_CACHE_KEY);
	}

	/**
	 * 从缓存中删除对象
	 *
	 * @param requestId
	 * @return
	 */
	public static void remove(String requestId) {
		RedisUtil.syncLock(sendConstant.SEND_CACHE_LOCK_KEY, sendConstant.SEND_CACHE_LOCK_TTL);
//		log.info("删除请求缓存：{}", requestId);
		RedisUtil.hdel(sendConstant.SEND_CACHE_KEY, requestId);

		RedisUtil.unLock(sendConstant.SEND_CACHE_LOCK_KEY);
	}


}
