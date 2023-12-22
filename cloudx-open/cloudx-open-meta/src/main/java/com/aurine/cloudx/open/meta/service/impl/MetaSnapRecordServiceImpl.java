package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaSnapRecordService;
import com.aurine.cloudx.open.origin.entity.ProjectSnapRecord;
import com.aurine.cloudx.open.origin.service.ProjectSnapRecordService;
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
public class MetaSnapRecordServiceImpl implements MetaSnapRecordService {

    @Resource
    private ProjectSnapRecordService projectSnapRecordService;


    @Override
    public R<ProjectSnapRecord> save(ProjectSnapRecord po) {
        boolean result = projectSnapRecordService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectSnapRecord> update(ProjectSnapRecord po) {
        boolean result = projectSnapRecordService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectSnapRecordService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
