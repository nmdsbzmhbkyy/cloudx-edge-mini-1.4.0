package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaPasswordInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.aurine.cloudx.open.origin.service.ProjectPasswdService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-密码信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPasswordInfoServiceImpl implements MetaPasswordInfoService {

    @Resource
    private ProjectPasswdService projectPasswdService;


    @Override
    public R<ProjectPasswd> save(ProjectPasswd po) {
        boolean result = projectPasswdService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPasswd> update(ProjectPasswd po) {
        boolean result = projectPasswdService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPasswdService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
