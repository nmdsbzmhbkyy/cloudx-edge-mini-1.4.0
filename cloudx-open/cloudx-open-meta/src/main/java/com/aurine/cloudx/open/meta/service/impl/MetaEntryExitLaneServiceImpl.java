package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaEntryExitLaneService;
import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import com.aurine.cloudx.open.origin.service.ProjectEntryExitLaneService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-出入口车道管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaEntryExitLaneServiceImpl implements MetaEntryExitLaneService {

    @Resource
    private ProjectEntryExitLaneService projectEntryExitLaneService;


    @Override
    public R<ProjectEntryExitLane> save(ProjectEntryExitLane po) {
        boolean result = projectEntryExitLaneService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectEntryExitLane> update(ProjectEntryExitLane po) {
        boolean result = projectEntryExitLaneService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectEntryExitLaneService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
