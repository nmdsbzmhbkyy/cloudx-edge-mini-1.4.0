package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkEntranceHisService;
import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.open.origin.service.ProjectParkEntranceHisService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车行记录管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkEntranceHisServiceImpl implements MetaParkEntranceHisService {

    @Resource
    private ProjectParkEntranceHisService projectParkEntranceHisService;


    @Override
    public R<ProjectParkEntranceHis> save(ProjectParkEntranceHis po) {
        boolean result = projectParkEntranceHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkEntranceHis> update(ProjectParkEntranceHis po) {
        boolean result = projectParkEntranceHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkEntranceHisService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
