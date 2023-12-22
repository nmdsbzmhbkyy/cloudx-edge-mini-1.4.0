package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaEntityLevelCfgService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.open.origin.service.ProjectEntityLevelCfgService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-组团配置管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaEntityLevelCfgServiceImpl implements MetaEntityLevelCfgService {

    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;


    @Override
    public R<ProjectEntityLevelCfg> save(ProjectEntityLevelCfg po) {
        boolean result = projectEntityLevelCfgService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectEntityLevelCfg> update(ProjectEntityLevelCfg po) {
        boolean result = projectEntityLevelCfgService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectEntityLevelCfgService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
