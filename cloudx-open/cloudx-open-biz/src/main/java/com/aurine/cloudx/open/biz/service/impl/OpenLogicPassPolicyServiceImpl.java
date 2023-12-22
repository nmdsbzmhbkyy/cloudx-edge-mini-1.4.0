package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenLogicPassPolicyService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.aurine.cloudx.open.origin.service.ProjectLogicPassPolicyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-逻辑策略管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenLogicPassPolicyServiceImpl implements OpenLogicPassPolicyService {

    @Resource
    private ProjectLogicPassPolicyService projectLogicPassPolicyService;


    @Override
    public R<LogicPassPolicyVo> getById(String id) {
        ProjectLogicPassPolicy po = projectLogicPassPolicyService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        LogicPassPolicyVo vo = new LogicPassPolicyVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<LogicPassPolicyVo>> page(Page page, LogicPassPolicyVo vo) {
        return R.ok(projectLogicPassPolicyService.page(page, vo));
    }

    @Override
    public R<LogicPassPolicyVo> save(LogicPassPolicyVo vo) {
        return R.ok();
    }

    @Override
    public R<LogicPassPolicyVo> update(LogicPassPolicyVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
