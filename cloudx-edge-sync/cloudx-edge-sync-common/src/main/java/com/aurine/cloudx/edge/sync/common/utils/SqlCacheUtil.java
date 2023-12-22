package com.aurine.cloudx.edge.sync.common.utils;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.edge.sync.common.constant.SqlCacheConstants;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 数据库缓存工具类
 * @author: weirm
 * @date: 2022/11/04 14:45
 **/
@Slf4j
public class SqlCacheUtil {

	/**
	 * 入库
	 *
	 * @param request
	 * @return
	 */
	public static synchronized long inTo(TaskInfo request) {
		RedisUtil.syncLock(SqlCacheConstants.SQL_CACHE_KEY_LOCK, SqlCacheConstants.SQL_CACHE_KEY_LOCK_TTL);
		RedisUtil.lSet(SqlCacheConstants.SQL_CACHE_KEY, request);
		long count = size();
		RedisUtil.unLock(SqlCacheConstants.SQL_CACHE_KEY_LOCK);

		return count;
	}

	/**
	 * 获取缓存数据,并删除当前缓存
	 *
	 * @return
	 */
	public synchronized static List<TaskInfo> list() {
		RedisUtil.syncLock(SqlCacheConstants.SQL_CACHE_KEY_LOCK, SqlCacheConstants.SQL_CACHE_KEY_LOCK_TTL);

		List<Object> objList = RedisUtil.lGet(SqlCacheConstants.SQL_CACHE_KEY, 0, -1);
		List<TaskInfo> resultList = new ArrayList<>();

		if (CollUtil.isNotEmpty(objList)) {
			for (Object object : objList) {
				resultList.add((TaskInfo) object);
			}
		}

		RedisUtil.del(SqlCacheConstants.SQL_CACHE_KEY);

		RedisUtil.unLock(SqlCacheConstants.SQL_CACHE_KEY_LOCK);
		return resultList;
	}

	/**
	 * 获取缓存大小
	 *
	 * @return
	 */
	public static long size() {
		return RedisUtil.lGetListSize(SqlCacheConstants.SQL_CACHE_KEY);
	}
}
