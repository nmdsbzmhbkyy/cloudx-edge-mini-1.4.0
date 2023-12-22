package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkBillingInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingInfo;
import com.aurine.cloudx.open.origin.service.ProjectParkBillingInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-缴费记录管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkBillingInfoServiceImpl implements MetaParkBillingInfoService {

    @Resource
    private ProjectParkBillingInfoService projectParkBillingInfoService;


    @Override
    public R<ProjectParkBillingInfo> save(ProjectParkBillingInfo po) {
        boolean result = projectParkBillingInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkBillingInfo> update(ProjectParkBillingInfo po) {
        boolean result = projectParkBillingInfoService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkBillingInfoService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
