package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectUnitInfo;
import com.aurine.cloudx.open.meta.service.MetaUnitInfoService;
import com.aurine.cloudx.open.origin.service.ProjectUnitInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-单元信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaUnitInfoServiceImpl implements MetaUnitInfoService {

    @Resource
    private ProjectUnitInfoService projectUnitInfoService;


    @Override
    public R<ProjectUnitInfo> save(ProjectUnitInfo po) {
        boolean result = projectUnitInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectUnitInfo> update(ProjectUnitInfo po) {
        boolean result = projectUnitInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectUnitInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
