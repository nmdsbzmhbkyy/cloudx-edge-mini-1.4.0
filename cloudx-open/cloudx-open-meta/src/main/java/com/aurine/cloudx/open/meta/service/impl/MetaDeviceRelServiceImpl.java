package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaDeviceRelService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.aurine.cloudx.open.origin.service.ProjectDeviceRelService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备关系管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaDeviceRelServiceImpl implements MetaDeviceRelService {

    @Resource
    private ProjectDeviceRelService projectDeviceRelService;


    @Override
    public R<ProjectDeviceRel> save(ProjectDeviceRel po) {
        boolean result = projectDeviceRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectDeviceRel> update(ProjectDeviceRel po) {
        boolean result = projectDeviceRelService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectDeviceRelService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
