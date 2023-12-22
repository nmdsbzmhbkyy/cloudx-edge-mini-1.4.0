package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParCarRegisterService;
import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
import com.aurine.cloudx.open.origin.service.ProjectParCarRegisterService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车辆登记管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParCarRegisterServiceImpl implements MetaParCarRegisterService {

    @Resource
    private ProjectParCarRegisterService projectParCarRegisterService;


    @Override
    public R<ProjectParCarRegister> save(ProjectParCarRegister po) {
        boolean result = projectParCarRegisterService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParCarRegister> update(ProjectParCarRegister po) {
        boolean result = projectParCarRegisterService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParCarRegisterService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
