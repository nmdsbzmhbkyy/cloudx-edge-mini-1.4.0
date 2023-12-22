package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
import com.aurine.cloudx.open.meta.service.MetaVisitorHistService;
import com.aurine.cloudx.open.origin.service.ProjectVisitorHisService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-来访记录管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaVisitorHistServiceImpl implements MetaVisitorHistService {

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;


    @Override
    public R<ProjectVisitorHis> save(ProjectVisitorHis po) {
        boolean result = projectVisitorHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectVisitorHis> update(ProjectVisitorHis po) {
        boolean result = projectVisitorHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectVisitorHisService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
