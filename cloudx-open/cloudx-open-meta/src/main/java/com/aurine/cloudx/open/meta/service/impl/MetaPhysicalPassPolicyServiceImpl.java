package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.aurine.cloudx.open.meta.service.MetaPhysicalPassPolicyService;
import com.aurine.cloudx.open.origin.service.ProjectPhysicalPassPolicyService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-物理策略管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaPhysicalPassPolicyServiceImpl implements MetaPhysicalPassPolicyService {

    @Resource
    private ProjectPhysicalPassPolicyService projectPhysicalPassPolicyService;


    @Override
    public R<ProjectPhysicalPassPolicy> save(ProjectPhysicalPassPolicy po) {
        boolean result = projectPhysicalPassPolicyService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectPhysicalPassPolicy> update(ProjectPhysicalPassPolicy po) {
        boolean result = projectPhysicalPassPolicyService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectPhysicalPassPolicyService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
