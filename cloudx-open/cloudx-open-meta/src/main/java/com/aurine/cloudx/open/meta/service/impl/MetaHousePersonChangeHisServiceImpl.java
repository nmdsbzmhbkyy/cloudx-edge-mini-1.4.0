package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaHousePersonChangeHisService;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonChangeHis;
import com.aurine.cloudx.open.origin.service.ProjectHousePersonChangeHisService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-房屋人员变更日志实现
 *
 * @author zouyu
 */
@Service
public class MetaHousePersonChangeHisServiceImpl implements MetaHousePersonChangeHisService {

    @Resource
    private ProjectHousePersonChangeHisService projectHousePersonChangeHisService;


    @Override
    public R<ProjectHousePersonChangeHis> save(ProjectHousePersonChangeHis po) {
        boolean result = projectHousePersonChangeHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }
}
