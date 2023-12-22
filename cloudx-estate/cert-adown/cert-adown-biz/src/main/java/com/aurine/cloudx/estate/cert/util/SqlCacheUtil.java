package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.constant.SqlCacheConstant;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 数据库缓存工具类
 * @author: wangwei
 * @date: 2021/12/27 16:50
 **/
@Slf4j
public class SqlCacheUtil {


	/**
	 * 加入修改缓存
	 *
	 * @param request
	 * @return
	 */
	public static synchronized long inToUpdateCache(SysCertAdownRequest request) {
		CertRedisUtil.syncLock(SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK, SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK_TTL);
		CertRedisUtil.lSet(SqlCacheConstant.UPDATE_SQL_CACHE_KEY, request);
		long count = updateSize();
		CertRedisUtil.unLock(SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK);

		return count;
	}


	/**
	 * 加入添加缓存
	 *
	 * @param request
	 * @return
	 */
	public static synchronized long inToInsertCache(SysCertAdownRequest request) {
		CertRedisUtil.syncLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK, SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK_TTL);
		CertRedisUtil.lSet(SqlCacheConstant.INSERT_SQL_CACHE_KEY, request);
		long count = updateSize();
		CertRedisUtil.unLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK);

		return count;
	}

	/**
	 * 加入添加缓存
	 *
	 * @param requestList
	 * @return
	 */
	public static synchronized long inToInsertCache(List<SysCertAdownRequest> requestList) {
		CertRedisUtil.syncLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK, SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK_TTL);
		CertRedisUtil.lSetList(SqlCacheConstant.INSERT_SQL_CACHE_KEY, requestList);
		long count = updateSize();
		CertRedisUtil.unLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK);

		return count;
	}

	/**
	 * 获取缓存数据,并删除当前缓存
	 *
	 * @return
	 */
	public synchronized static List<SysCertAdownRequest> updateList() {
		CertRedisUtil.syncLock(SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK, SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK_TTL);

		List<Object> objList = CertRedisUtil.lGet(SqlCacheConstant.UPDATE_SQL_CACHE_KEY, 0, -1);
		List<SysCertAdownRequest> resultList = new ArrayList<>();

		if (CollUtil.isNotEmpty(objList)) {
			for (Object object : objList) {
				resultList.add((SysCertAdownRequest) object);
			}
		}

		CertRedisUtil.del(SqlCacheConstant.UPDATE_SQL_CACHE_KEY);

		CertRedisUtil.unLock(SqlCacheConstant.UPDATE_SQL_CACHE_KEY_LOCK);
		return resultList;
	}

	/**
	 * 获取缓存数据,并删除当前缓存
	 *
	 * @return
	 */
	public synchronized static List<SysCertAdownRequest> insertList() {
		CertRedisUtil.syncLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK, SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK_TTL);

		List<Object> objList = CertRedisUtil.lGet(SqlCacheConstant.INSERT_SQL_CACHE_KEY, 0, -1);
		List<SysCertAdownRequest> resultList = new ArrayList<>();

		if (CollUtil.isNotEmpty(objList)) {
			for (Object object : objList) {
				resultList.add((SysCertAdownRequest) object);
			}
		}

		CertRedisUtil.del(SqlCacheConstant.INSERT_SQL_CACHE_KEY);

		CertRedisUtil.unLock(SqlCacheConstant.INSERT_SQL_CACHE_KEY_LOCK);
		return resultList;
	}

	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long updateSize() {
		return CertRedisUtil.lGetListSize(SqlCacheConstant.UPDATE_SQL_CACHE_KEY);
	}

	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long insertSize() {
		return CertRedisUtil.lGetListSize(SqlCacheConstant.INSERT_SQL_CACHE_KEY);
	}


}
