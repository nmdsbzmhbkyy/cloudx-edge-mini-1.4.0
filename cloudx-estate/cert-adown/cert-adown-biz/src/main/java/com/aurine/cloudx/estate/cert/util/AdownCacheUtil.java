package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @description: 下发缓存工具类
 * 用户缓存下发请求信息
 * @author: wangwei
 * @date: 2021/12/23 14:26
 **/
@Slf4j
public class AdownCacheUtil {


	/**
	 * 入库
	 *
	 * @param request
	 * @return
	 */
	public static boolean inTo(SysCertAdownRequest request) {
		return CertRedisUtil.hset(AdownConstant.ADOWN_CACHE_KEY, request.getRequestId(), request);
	}

	/**
	 * 获取缓存数据
	 *
	 * @return
	 */
	public static List<SysCertAdownRequest> list() {

		Map<Object, Object> cacheMap = CertRedisUtil.hmget(AdownConstant.ADOWN_CACHE_KEY);
		List<SysCertAdownRequest> list = new ArrayList<>();

		if (CollUtil.isNotEmpty(cacheMap)) {
			for (Object obj : cacheMap.values()) {
				list.add((SysCertAdownRequest) obj);
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
	public static SysCertAdownRequest get(String requestId) {
		Object obj = CertRedisUtil.hget(AdownConstant.ADOWN_CACHE_KEY, requestId);
		SysCertAdownRequest result = null;

		if (obj != null) {
//			RedisUtil.hdel(AdownConstant.ADOWN_CACHE_KEY, obj);
			result = (SysCertAdownRequest) obj;
		}

		return result;
	}


	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long size() {
		return CertRedisUtil.hSize(AdownConstant.ADOWN_CACHE_KEY);
	}

	/**
	 * 从缓存中删除对象
	 *
	 * @param requestId
	 * @return
	 */
	public static void remove(String requestId) {
		CertRedisUtil.syncLock(AdownConstant.ADOWN_CACHE_LOCK_KEY, AdownConstant.ADOWN_CACHE_LOCK_TTL);
//		log.info("删除请求缓存：{}", requestId);
		CertRedisUtil.hdel(AdownConstant.ADOWN_CACHE_KEY, requestId);

		CertRedisUtil.unLock(AdownConstant.ADOWN_CACHE_LOCK_KEY);
	}


}
