package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaBlacklistService;
import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.aurine.cloudx.open.origin.service.ProjectBlacklistService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-黑名单管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaBlacklistServiceImpl implements MetaBlacklistService {

    @Resource
    private ProjectBlacklistService projectBlacklistService;


    @Override
    public R<ProjectBlacklist> save(ProjectBlacklist po) {
        boolean result = projectBlacklistService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectBlacklist> update(ProjectBlacklist po) {
        boolean result = projectBlacklistService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectBlacklistService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
