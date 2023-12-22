package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaDeviceParamInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceParamInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备参数
 *
 * @author : zouyu
 * @date : 2022-07-28 10:19:23
 */
@Service
public class MetaDeviceParamInfoServiceImpl implements MetaDeviceParamInfoService {

    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;


    @Override
    public R<ProjectDeviceParamInfo> save(ProjectDeviceParamInfo po) {
        boolean result = projectDeviceParamInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectDeviceParamInfo> update(ProjectDeviceParamInfo po) {
        boolean result = projectDeviceParamInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }
    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectDeviceParamInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
