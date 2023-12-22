package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenCardInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.service.ProjectCardService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenCardInfoServiceImpl implements OpenCardInfoService {

    @Resource
    private ProjectCardService projectCardService;


    @Override
    public R<CardInfoVo> getById(String id) {
        ProjectCard po = projectCardService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        CardInfoVo vo = new CardInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<CardInfoVo>> page(Page page, CardInfoVo vo) {
        return R.ok(projectCardService.page(page, vo));
    }

    @Override
    public R<CardInfoVo> save(CardInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<CardInfoVo> update(CardInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
