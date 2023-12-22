package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaDeviceSubSystemService;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.aurine.cloudx.open.origin.service.ProjectDeviceSubsystemService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-设备子系统管理实现
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@Service
public class MetaDeviceSubSystemServiceImpl implements MetaDeviceSubSystemService {

    @Resource
    private ProjectDeviceSubsystemService projectDeviceSubsystemService;


    @Override
    public R<ProjectDeviceSubsystem> save(ProjectDeviceSubsystem po) {
        boolean result = projectDeviceSubsystemService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);
        
        return R.ok(po);
    }
}
