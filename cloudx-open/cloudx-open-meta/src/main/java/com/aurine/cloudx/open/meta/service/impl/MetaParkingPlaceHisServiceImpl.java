package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkingPlaceHisService;
import com.aurine.cloudx.open.origin.entity.ProjectParkingPlaceHis;
import com.aurine.cloudx.open.origin.service.ProjectParkingPlaceHisService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车位变动记录管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkingPlaceHisServiceImpl implements MetaParkingPlaceHisService {

    @Resource
    private ProjectParkingPlaceHisService projectParkingPlaceHisService;


    @Override
    public R<ProjectParkingPlaceHis> save(ProjectParkingPlaceHis po) {
        boolean result = projectParkingPlaceHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkingPlaceHis> update(ProjectParkingPlaceHis po) {
        boolean result = projectParkingPlaceHisService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkingPlaceHisService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
