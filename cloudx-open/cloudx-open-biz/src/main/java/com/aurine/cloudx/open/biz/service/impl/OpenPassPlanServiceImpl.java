package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPassPlanService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPassPlanServiceImpl implements OpenPassPlanService {

    @Resource
    private ProjectPassPlanService projectPassPlanService;


    @Override
    public R<PassPlanVo> getById(String id) {
        ProjectPassPlan po = projectPassPlanService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PassPlanVo vo = new PassPlanVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PassPlanVo>> page(Page page, PassPlanVo vo) {
        return R.ok(projectPassPlanService.page(page, vo));
    }

    @Override
    public R<PassPlanVo> save(PassPlanVo vo) {
        return R.ok();
    }

    @Override
    public R<PassPlanVo> update(PassPlanVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
