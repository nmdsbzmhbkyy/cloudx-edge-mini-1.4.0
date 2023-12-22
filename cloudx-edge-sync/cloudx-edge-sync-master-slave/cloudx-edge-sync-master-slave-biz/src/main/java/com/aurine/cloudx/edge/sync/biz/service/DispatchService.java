package com.aurine.cloudx.edge.sync.biz.service;



public interface DispatchService {

    /**
     * 调度事件由入库、队列消费为触发契机
     * 根据调度策略，从数据库中获取下发缓存队列
     */
    void dispatchQueue(String serviceType, String projectUuid);

}
