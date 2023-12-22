package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceEvent;
import com.aurine.cloudx.open.meta.service.MetaPersonEntranceService;
import com.aurine.cloudx.open.origin.service.ProjectEntranceEventService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人行事件管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPersonEntranceServiceImpl implements MetaPersonEntranceService {

    @Resource
    private ProjectEntranceEventService projectEntranceEventService;


    @Override
    public R<ProjectEntranceEvent> save(ProjectEntranceEvent po) {
        boolean result = projectEntranceEventService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectEntranceEvent> update(ProjectEntranceEvent po) {
        boolean result = projectEntranceEventService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectEntranceEventService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
