package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenPersonInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.PersonInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonInfo;
import com.aurine.cloudx.open.origin.service.ProjectPersonInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenPersonInfoServiceImpl implements OpenPersonInfoService {

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;


    @Override
    public R<PersonInfoVo> getById(String id) {
        ProjectPersonInfo po = projectPersonInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        PersonInfoVo vo = new PersonInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<PersonInfoVo>> page(Page page, PersonInfoVo vo) {
        return R.ok(projectPersonInfoService.page(page, vo));
    }

    @Override
    public R<PersonInfoVo> save(PersonInfoVo vo) {
//        ProjectPersonInfo po = new ProjectPersonInfo();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectPersonInfoService.save(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.SYSTEM_ERROR);
//
//        PersonInfoVo resultVo = new PersonInfoVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<PersonInfoVo> update(PersonInfoVo vo) {
//        ProjectPersonInfo po = new ProjectPersonInfo();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectPersonInfoService.updateById(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        PersonInfoVo resultVo = new PersonInfoVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
//        boolean result = projectPersonInfoService.removeById(id);
//        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        return R.ok(true);

        return R.ok();
    }
}
