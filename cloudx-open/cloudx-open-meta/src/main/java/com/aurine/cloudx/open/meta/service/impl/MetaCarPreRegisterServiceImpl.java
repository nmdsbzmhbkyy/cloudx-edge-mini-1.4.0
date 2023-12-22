package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaCarPreRegisterService;
import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.aurine.cloudx.open.origin.service.ProjectCarPreRegisterService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车辆登记记录管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaCarPreRegisterServiceImpl implements MetaCarPreRegisterService {

    @Resource
    private ProjectCarPreRegisterService projectCarPreRegisterService;


    @Override
    public R<ProjectCarPreRegister> save(ProjectCarPreRegister po) {
        boolean result = projectCarPreRegisterService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectCarPreRegister> update(ProjectCarPreRegister po) {
        boolean result = projectCarPreRegisterService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectCarPreRegisterService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
