package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaDeviceAttrService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
import com.aurine.cloudx.open.origin.service.ProjectDeviceAttrService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备拓展属性管理实现
 *
 * @author : zouyu
 * @date : 2023-05-04 14:00:55
 */

@Service
public class MetaDeviceAttrServiceImpl implements MetaDeviceAttrService {

    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;


    @Override
    public R<ProjectDeviceAttr> save(ProjectDeviceAttr po) {
        boolean result = projectDeviceAttrService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectDeviceAttr> update(ProjectDeviceAttr po) {
        boolean result = projectDeviceAttrService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectDeviceAttrService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
