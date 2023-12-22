package com.aurine.cloudx.open.cascade.service.impl;

import com.aurine.cloudx.open.cascade.service.CascadeSyncLogService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.aurine.cloudx.open.origin.service.EdgeSyncLogService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 级联入云同步日志
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@Service
public class CascadeSyncLogServiceImpl implements CascadeSyncLogService {

    @Resource
    private EdgeSyncLogService edgeSyncLogService;


    @Override
    public R<EdgeSyncLog> save(EdgeSyncLog po) {
        boolean result = edgeSyncLogService.save(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<EdgeSyncLog> update(EdgeSyncLog po) {
        boolean result = edgeSyncLogService.updateById(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }
}
