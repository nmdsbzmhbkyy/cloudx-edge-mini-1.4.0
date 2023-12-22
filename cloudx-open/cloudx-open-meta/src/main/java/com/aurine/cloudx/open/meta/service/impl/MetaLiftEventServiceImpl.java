package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaDeviceRelService;
import com.aurine.cloudx.open.meta.service.MetaLiftEventService;
import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.aurine.cloudx.open.origin.service.ProjectLiftEventService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备关系管理实现
 *
 * @author : zouyu
 * @date : 2022-07-18 09:39:48
 */
@Service
public class MetaLiftEventServiceImpl implements MetaLiftEventService {

    @Resource
    private ProjectLiftEventService projectLiftEventService;


    @Override
    public R<ProjectLiftEvent> save(ProjectLiftEvent po) {
        boolean result = projectLiftEventService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }
}
