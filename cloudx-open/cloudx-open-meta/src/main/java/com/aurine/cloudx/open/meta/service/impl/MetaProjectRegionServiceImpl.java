package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.meta.service.MetaProjectRegionService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceRegionService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-项目区域管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaProjectRegionServiceImpl implements MetaProjectRegionService {

    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;


    @Override
    public R<ProjectDeviceRegion> save(ProjectDeviceRegion po) {
        boolean result = projectDeviceRegionService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectDeviceRegion> update(ProjectDeviceRegion po) {
        boolean result = projectDeviceRegionService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectDeviceRegionService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
