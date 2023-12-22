package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaParkBillingRuleService;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingRule;
import com.aurine.cloudx.open.origin.service.ProjectParkBillingRuleService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-车场计费规则管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaParkBillingRuleServiceImpl implements MetaParkBillingRuleService {

    @Resource
    private ProjectParkBillingRuleService projectParkBillingRuleService;


    @Override
    public R<ProjectParkBillingRule> save(ProjectParkBillingRule po) {
        boolean result = projectParkBillingRuleService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectParkBillingRule> update(ProjectParkBillingRule po) {
        boolean result = projectParkBillingRuleService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectParkBillingRuleService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
