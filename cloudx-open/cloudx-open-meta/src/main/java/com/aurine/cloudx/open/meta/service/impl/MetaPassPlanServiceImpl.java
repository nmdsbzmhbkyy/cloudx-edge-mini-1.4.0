package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaPassPlanService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-通行方案管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPassPlanServiceImpl implements MetaPassPlanService {

    @Resource
    private ProjectPassPlanService projectPassPlanService;


    @Override
    public R<ProjectPassPlan> save(ProjectPassPlan po) {
        boolean result = projectPassPlanService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPassPlan> update(ProjectPassPlan po) {
        boolean result = projectPassPlanService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPassPlanService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
