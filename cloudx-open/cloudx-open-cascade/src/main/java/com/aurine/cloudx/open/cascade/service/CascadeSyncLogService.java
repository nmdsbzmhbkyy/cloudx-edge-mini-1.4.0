package com.aurine.cloudx.open.cascade.service;

import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 级联入云同步日志
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface CascadeSyncLogService {

    /**
     * 新增级联入云同步日志
     *
     * @param po
     * @return
     */
    R<EdgeSyncLog> save(EdgeSyncLog po);

    /**
     * 修改级联入云同步日志
     *
     * @param po
     * @return
     */
    R<EdgeSyncLog> update(EdgeSyncLog po);
}
