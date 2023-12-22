package com.aurine.cloudx.estate.openapi.sync.service;

/**
 * @author：zouyu
 * @data: 2022/3/22 16:28
 */
public interface SyncService {

    /**
     * 执行同步任务
     *
     * @param projectId
     * @param projectUUID
     */
    void perform(Integer projectId, String projectUUID);

    /**
     * 获取版本
     *
     * @return
     */
    String getVersion();
}
