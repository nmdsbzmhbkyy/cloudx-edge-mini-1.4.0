package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaBuildingInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
import com.aurine.cloudx.open.origin.service.ProjectBuildingInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-楼栋信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaBuildingInfoServiceImpl implements MetaBuildingInfoService {

    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    @Override
    public R<ProjectBuildingInfo> save(ProjectBuildingInfo po) {
        boolean result = projectBuildingInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectBuildingInfo> update(ProjectBuildingInfo po) {
        boolean result = projectBuildingInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectBuildingInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
