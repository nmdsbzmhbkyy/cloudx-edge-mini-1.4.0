package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaVehiclesEntryExitService;
import com.aurine.cloudx.open.origin.entity.ProjectVehiclesEntryExit;
import com.aurine.cloudx.open.origin.service.ProjectVehiclesEntryExitService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车辆出入口信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaVehiclesEntryExitServiceImpl implements MetaVehiclesEntryExitService {

    @Resource
    private ProjectVehiclesEntryExitService projectVehiclesEntryExitService;


    @Override
    public R<ProjectVehiclesEntryExit> save(ProjectVehiclesEntryExit po) {
        boolean result = projectVehiclesEntryExitService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectVehiclesEntryExit> update(ProjectVehiclesEntryExit po) {
        boolean result = projectVehiclesEntryExitService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectVehiclesEntryExitService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
