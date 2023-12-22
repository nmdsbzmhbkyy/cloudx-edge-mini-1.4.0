package com.aurine.cloudx.estate.cert.util;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.constant.QueueConstant;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 队列工具类
 * @author: wangwei
 * @date: 2021/12/17 9:39
 **/
@Slf4j
public class QueueUtil {

	/**
	 * 数据入列
	 *
	 * @param request
	 * @return
	 */
	public static long inToQueue(SysCertAdownRequest request) {
		List<SysCertAdownRequest> list = new ArrayList<>();
		list.add(request);
		return inToQueue(list);
	}

	/**
	 * 数据入列
	 *
	 * @param requestList
	 * @return
	 */
	public static long inToQueue(List<SysCertAdownRequest> requestList) {

		long resultCount = 0;
		if (CollUtil.isNotEmpty(requestList)) {

			for (SysCertAdownRequest request : requestList) {
				CertRedisUtil.lSet(QueueConstant.QUEUE_NAME, request);
			}
		}


		resultCount = CertRedisUtil.lGetListSize(QueueConstant.QUEUE_NAME);

		if (resultCount >= 1) {
			log.info("队列已写入{}条，当前队列容量为：{}", requestList.size(), resultCount);
		}

		return resultCount;
	}

	/**
	 * 获取当前队列
	 *
	 * @return
	 */
	public static List<SysCertAdownRequest> list() {
		List<Object> objList = CertRedisUtil.lGet(QueueConstant.QUEUE_NAME, 0, -1);
		List<SysCertAdownRequest> resultList = new ArrayList<>();
		if (CollUtil.isNotEmpty(objList)) {
			for (Object obj : objList) {
				resultList.add((SysCertAdownRequest) obj);
			}

		}
		return resultList;
	}


	/**
	 * 获取当前队列长度
	 *
	 * @return
	 */
	public static long getQueueSize() {
		if (CertRedisUtil.hasKey(QueueConstant.QUEUE_NAME)) {
			return CertRedisUtil.lGetListSize(QueueConstant.QUEUE_NAME);
		}
		return 0;
	}

	/**
	 * 同步方式获取1条最新的数据
	 * 当队列中存在任务，且流量桶中存在token时，获取最新的一条下发请求数据
	 *
	 * @return
	 */
	public static SysCertAdownRequest getLatestSync() {
		SysCertAdownRequest result = null;
		CertRedisUtil.syncLock(QueueConstant.QUEUE_LOCK, QueueConstant.QUEUE_LOCK_TTL);


		if (getQueueSize() >= 1) {

			//如果没有令牌，则不分配任务
			if (TokenBucketUtil.getToken()) {
				Object sysCertAdownRequest = CertRedisUtil.lGetIndex(QueueConstant.QUEUE_NAME, 0);
				CertRedisUtil.lRemove(QueueConstant.QUEUE_NAME, 1, sysCertAdownRequest);
				result = (SysCertAdownRequest) sysCertAdownRequest;
			} else {
				log.debug("获取下发请求失败，当前无可用token");
			}
		} else {
			log.debug("队列中无任务可下发");
		}


		CertRedisUtil.unLock(QueueConstant.QUEUE_LOCK);
		return result;
	}
}
