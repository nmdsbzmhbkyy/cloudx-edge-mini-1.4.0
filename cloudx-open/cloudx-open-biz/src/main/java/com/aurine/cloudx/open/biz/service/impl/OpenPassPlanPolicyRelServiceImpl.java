package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPassPlanPolicyRelService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PassPlanPolicyRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanPolicyRelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPassPlanPolicyRelServiceImpl implements OpenPassPlanPolicyRelService {

    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;


    @Override
    public R<PassPlanPolicyRelVo> getById(String id) {
        ProjectPassPlanPolicyRel po = projectPassPlanPolicyRelService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PassPlanPolicyRelVo vo = new PassPlanPolicyRelVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PassPlanPolicyRelVo>> page(Page page, PassPlanPolicyRelVo vo) {
        return R.ok(projectPassPlanPolicyRelService.page(page, vo));
    }

    @Override
    public R<PassPlanPolicyRelVo> save(PassPlanPolicyRelVo vo) {
        return R.ok();
    }

    @Override
    public R<PassPlanPolicyRelVo> update(PassPlanPolicyRelVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
