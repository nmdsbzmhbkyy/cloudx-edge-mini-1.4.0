package com.aurine.cloudx.edge.sync.biz.service;



/**
 * @Author: wrm
 * @Date: 2022/1/26 16:15
 * @Version: 1.0
 * @Remarks:
 **/
public interface DispatchService {

	/**
	 * 调度事件由入库、队列消费为触发契机
	 * 根据调度策略，从数据库中获取下发缓存队列
	 */
	void dispatchQueue(String serviceType, String projectUuid);

}
