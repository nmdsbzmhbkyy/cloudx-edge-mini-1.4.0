package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectPersonDevice;
import com.aurine.cloudx.open.meta.service.MetaPersonDeviceRelService;
import com.aurine.cloudx.open.origin.service.ProjectPersonDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员通行方案关系管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPersonDeviceRelServiceImpl implements MetaPersonDeviceRelService {

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;


    @Override
    public R<ProjectPersonDevice> save(ProjectPersonDevice po) {
        boolean result = projectPersonDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPersonDevice> update(ProjectPersonDevice po) {
        boolean result = projectPersonDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPersonDeviceService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
