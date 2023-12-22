package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaFrameInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.aurine.cloudx.open.origin.service.ProjectFrameInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-框架信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaFrameInfoServiceImpl implements MetaFrameInfoService {

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;


    @Override
    public R<ProjectFrameInfo> save(ProjectFrameInfo po) {
        boolean result = projectFrameInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectFrameInfo> update(ProjectFrameInfo po) {
        boolean result = projectFrameInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectFrameInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
