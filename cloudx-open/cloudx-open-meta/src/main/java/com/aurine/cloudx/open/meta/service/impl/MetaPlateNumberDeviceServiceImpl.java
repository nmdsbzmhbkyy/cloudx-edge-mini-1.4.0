package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaPlateNumberDeviceService;
import com.aurine.cloudx.open.origin.entity.ProjectPlateNumberDevice;
import com.aurine.cloudx.open.origin.service.ProjectPlateNumberDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备车牌号下发情况管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPlateNumberDeviceServiceImpl implements MetaPlateNumberDeviceService {

    @Resource
    private ProjectPlateNumberDeviceService projectPlateNumberDeviceService;


    @Override
    public R<ProjectPlateNumberDevice> save(ProjectPlateNumberDevice po) {
        boolean result = projectPlateNumberDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPlateNumberDevice> update(ProjectPlateNumberDevice po) {
        boolean result = projectPlateNumberDeviceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPlateNumberDeviceService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
