package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaCardHisService;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.service.ProjectCardHisService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 卡操作记录
 *
 * @author : zy
 * @date : 2022-11-14 11:25:20
 */

@Service
public class MetaCardHisServiceImpl implements MetaCardHisService {


    @Resource
    private ProjectCardHisService projectCardHisService;


    @Override
    public R<ProjectCardHis> save(ProjectCardHis po) {
        boolean result = projectCardHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }


    @Override
    public R<ProjectCardHis> update(ProjectCardHis po) {
        boolean result = projectCardHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);
        return R.ok(po);
    }
}
