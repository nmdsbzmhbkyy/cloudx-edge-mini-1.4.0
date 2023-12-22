package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPersonPlanRelService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.open.origin.service.ProjectPersonPlanRelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenPersonPlanRelServiceImpl implements OpenPersonPlanRelService {

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;


    @Override
    public R<PersonPlanRelVo> getById(String id) {
        ProjectPersonPlanRel po = projectPersonPlanRelService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PersonPlanRelVo vo = new PersonPlanRelVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PersonPlanRelVo>> page(Page page, PersonPlanRelVo vo) {
        return R.ok(projectPersonPlanRelService.page(page, vo));
    }

    @Override
    public R<PersonPlanRelVo> save(PersonPlanRelVo vo) {
        return R.ok();
    }

    @Override
    public R<PersonPlanRelVo> update(PersonPlanRelVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
