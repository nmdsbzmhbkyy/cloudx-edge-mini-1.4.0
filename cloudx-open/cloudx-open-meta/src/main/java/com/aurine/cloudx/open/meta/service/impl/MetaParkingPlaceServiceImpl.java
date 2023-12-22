package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkingPlaceService;
import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.aurine.cloudx.open.origin.service.ProjectParkingPlaceService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车位管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkingPlaceServiceImpl implements MetaParkingPlaceService {

    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;


    @Override
    public R<ProjectParkingPlace> save(ProjectParkingPlace po) {
        boolean result = projectParkingPlaceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkingPlace> update(ProjectParkingPlace po) {
        boolean result = projectParkingPlaceService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkingPlaceService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
