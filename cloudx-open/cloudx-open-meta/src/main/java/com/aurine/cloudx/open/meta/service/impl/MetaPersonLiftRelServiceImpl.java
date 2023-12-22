package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaPersonLiftRelService;
import com.aurine.cloudx.open.origin.entity.ProjectPersonLiftRel;
import com.aurine.cloudx.open.origin.service.ProjectPersonLiftRelService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员电梯权限关系
 *
 * @author : zouyu
 * @date : 2022-07-28 10:19:23
 */
@Service
public class MetaPersonLiftRelServiceImpl implements MetaPersonLiftRelService {

    @Resource
    private ProjectPersonLiftRelService projectPersonLiftRelService;


    @Override
    public R<ProjectPersonLiftRel> save(ProjectPersonLiftRel po) {
        boolean result = projectPersonLiftRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPersonLiftRel> update(ProjectPersonLiftRel po) {
        boolean result = projectPersonLiftRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPersonLiftRelService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
