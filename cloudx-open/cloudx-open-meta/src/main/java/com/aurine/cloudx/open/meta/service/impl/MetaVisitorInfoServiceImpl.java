package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectVisitor;
import com.aurine.cloudx.open.meta.service.MetaVisitorInfoService;
import com.aurine.cloudx.open.origin.service.ProjectVisitorService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-访客信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaVisitorInfoServiceImpl implements MetaVisitorInfoService {

    @Resource
    private ProjectVisitorService projectVisitorService;


    @Override
    public R<ProjectVisitor> save(ProjectVisitor po) {
        boolean result = projectVisitorService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectVisitor> update(ProjectVisitor po) {
        boolean result = projectVisitorService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectVisitorService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
