package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkCarTypeService;
import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
import com.aurine.cloudx.open.origin.service.ProjectParkCarTypeService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车辆类型管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkCarTypeServiceImpl implements MetaParkCarTypeService {

    @Resource
    private ProjectParkCarTypeService projectParkCarTypeService;


    @Override
    public R<ProjectParkCarType> save(ProjectParkCarType po) {
        boolean result = projectParkCarTypeService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkCarType> update(ProjectParkCarType po) {
        boolean result = projectParkCarTypeService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkCarTypeService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
