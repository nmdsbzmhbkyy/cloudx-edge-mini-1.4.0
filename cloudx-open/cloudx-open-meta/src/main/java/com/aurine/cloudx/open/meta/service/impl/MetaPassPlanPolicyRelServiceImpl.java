package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaPassPlanPolicyRelService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanPolicyRelService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-通行方案策略关系管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPassPlanPolicyRelServiceImpl implements MetaPassPlanPolicyRelService {

    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;


    @Override
    public R<ProjectPassPlanPolicyRel> save(ProjectPassPlanPolicyRel po) {
        boolean result = projectPassPlanPolicyRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPassPlanPolicyRel> update(ProjectPassPlanPolicyRel po) {
        boolean result = projectPassPlanPolicyRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPassPlanPolicyRelService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
