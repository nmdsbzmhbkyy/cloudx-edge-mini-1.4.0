package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @description: 下发时序工具类
 * 用于操作下发任务时间序列存储，使用redis zset存储
 * 下发时序用于监控下发任务的先后顺序，以及超时状态
 * @author: wangwei
 * @date: 2021/12/23 14:26
 **/
@Slf4j
public class AdownSequenceUtil {


	public static List<String> list() {
		Set<String> set = CertRedisUtil.zRange(AdownConstant.ADOWN_SEQUENCE_KEY, 0, -1);
		List<String> list = null;
		if (CollUtil.isNotEmpty(set)) {
			list = set.stream().collect(Collectors.toList());
		}
		return list;
	}

	/**
	 * 获取超时时间
	 *
	 * @return
	 */
	public static Double getOvertime(long ttl) {
		return Double.valueOf(System.currentTimeMillis() + ttl * 1000);
	}

	/**
	 * 任务已完成时，删除对应任务数据
	 *
	 * @param requestId
	 * @return
	 */
	public synchronized static void remove(String requestId) {
		CertRedisUtil.syncLock(AdownConstant.ADOWN_SEQUENCE_LOCK_KEY, AdownConstant.ADOWN_SEQUENCE_LOCK_TTL);
		CertRedisUtil.zRemove(AdownConstant.ADOWN_SEQUENCE_KEY, requestId);
		CertRedisUtil.unLock(AdownConstant.ADOWN_SEQUENCE_LOCK_KEY);
	}

	/**
	 * 入库
	 *
	 * @param requestId 请求id
	 * @param ttl       超时时间
	 * @return
	 */
	public static boolean inTo(String requestId, long ttl) {
		return CertRedisUtil.zSet(AdownConstant.ADOWN_SEQUENCE_KEY, requestId, getOvertime(ttl));
	}

	/**
	 * 超时校验，获取时间戳超过当前时间节点的数据
	 * 数据为空时，说明当前无超时数据
	 *
	 * @return
	 */
	public synchronized static String[] getTimeoutRequest() {
		String[] resultArray = null;

		CertRedisUtil.syncLock(AdownConstant.ADOWN_SEQUENCE_LOCK_KEY, AdownConstant.ADOWN_SEQUENCE_LOCK_TTL);

		//获取过期的序列对象
		Set<String> set = CertRedisUtil.zRangeWithScores(AdownConstant.ADOWN_SEQUENCE_KEY, 0, getOvertime(0));

		if (CollUtil.isNotEmpty(set)) {
			resultArray = new String[set.size()];
			int i = 0;
			for (String reqId : set) {
				resultArray[i++] = reqId;
			}
			CertRedisUtil.zRemove(AdownConstant.ADOWN_SEQUENCE_KEY, resultArray);//过期数据移除序列
		}

		CertRedisUtil.unLock(AdownConstant.ADOWN_SEQUENCE_LOCK_KEY);

		return resultArray;
	}


	/**
	 * 获取时序中最后一位（最早写入的一个）
	 *
	 * @return
	 */
	public static String getLast(String sequenceName) {
		Set<String> set = CertRedisUtil.zRangeWithScores(AdownConstant.ADOWN_SEQUENCE_KEY, 0, getOvertime(0));
		if (CollUtil.isNotEmpty(set)) {
			return set.stream().findFirst().get();
		} else {
			return null;
		}
	}

	/**
	 * 获取当前序列长度
	 *
	 * @return
	 */
	public static long size() {
		return CertRedisUtil.zSize(AdownConstant.ADOWN_SEQUENCE_KEY);
	}


}
