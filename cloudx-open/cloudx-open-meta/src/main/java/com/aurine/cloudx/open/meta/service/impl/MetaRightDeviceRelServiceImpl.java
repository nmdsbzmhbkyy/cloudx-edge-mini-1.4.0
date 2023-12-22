package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.meta.service.MetaRightDeviceRelService;
import com.aurine.cloudx.open.origin.service.ProjectRightDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-权限设备关系管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaRightDeviceRelServiceImpl implements MetaRightDeviceRelService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;


    @Override
    public R<ProjectRightDevice> save(ProjectRightDevice po) {
        boolean result = projectRightDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectRightDevice> update(ProjectRightDevice po) {
        boolean result = projectRightDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectRightDeviceService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
