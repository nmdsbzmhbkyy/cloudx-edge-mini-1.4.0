package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.pig4cloud.pigx.common.core.util.R;
import com.aurine.cloudx.open.meta.service.MetaAlarmEventService;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.open.origin.service.ProjectEntranceAlarmEventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-报警事件管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaAlarmEventServiceImpl implements MetaAlarmEventService {

    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;

    @Override
    public R<ProjectEntranceAlarmEvent> save(ProjectEntranceAlarmEvent po) {
        boolean result = projectEntranceAlarmEventService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectEntranceAlarmEvent> update(ProjectEntranceAlarmEvent po) {
        boolean result = projectEntranceAlarmEventService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectEntranceAlarmEventService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
