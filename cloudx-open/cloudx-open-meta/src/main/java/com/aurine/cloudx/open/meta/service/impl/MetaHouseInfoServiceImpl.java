package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaHouseInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.aurine.cloudx.open.origin.service.ProjectHouseInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-房屋信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaHouseInfoServiceImpl implements MetaHouseInfoService {

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;


    @Override
    public R<ProjectHouseInfo> save(ProjectHouseInfo po) {
        boolean result = projectHouseInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectHouseInfo> update(ProjectHouseInfo po) {
        boolean result = projectHouseInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectHouseInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
