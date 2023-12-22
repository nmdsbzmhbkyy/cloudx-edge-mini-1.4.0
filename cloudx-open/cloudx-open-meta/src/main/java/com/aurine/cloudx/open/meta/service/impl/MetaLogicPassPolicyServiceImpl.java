package com.aurine.cloudx.open.meta.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaLogicPassPolicyService;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.aurine.cloudx.open.origin.service.ProjectLogicPassPolicyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * open平台-逻辑策略管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class MetaLogicPassPolicyServiceImpl implements MetaLogicPassPolicyService {

    @Resource
    private ProjectLogicPassPolicyService projectLogicPassPolicyService;


    @Override
    public R<ProjectLogicPassPolicy> save(ProjectLogicPassPolicy po) {
        boolean result;
        Integer projectId = po.getProjectId();
        if (projectId == null) {
            result = projectLogicPassPolicyService.save(po);
        } else {
            List<ProjectLogicPassPolicy> list = projectLogicPassPolicyService.list(Wrappers.lambdaQuery(ProjectLogicPassPolicy.class)
                    .eq(ProjectLogicPassPolicy::getPolicyId, po.getPolicyId())
                    .eq(ProjectLogicPassPolicy::getProjectId, projectId));
            result = CollUtil.isEmpty(list) ? projectLogicPassPolicyService.save(po) : projectLogicPassPolicyService.updateById(po);
        }
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectLogicPassPolicy> update(ProjectLogicPassPolicy po) {
        boolean result;
        Integer projectId = po.getProjectId();
        if (projectId == null) {
            result = projectLogicPassPolicyService.updateById(po);
        } else {
            List<ProjectLogicPassPolicy> list = projectLogicPassPolicyService.list(Wrappers.lambdaQuery(ProjectLogicPassPolicy.class)
                    .eq(ProjectLogicPassPolicy::getPolicyId, po.getPolicyId())
                    .eq(ProjectLogicPassPolicy::getProjectId, projectId));
            result = CollUtil.isEmpty(list) ? projectLogicPassPolicyService.save(po) : projectLogicPassPolicyService.updateById(po);
        }
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectLogicPassPolicyService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
