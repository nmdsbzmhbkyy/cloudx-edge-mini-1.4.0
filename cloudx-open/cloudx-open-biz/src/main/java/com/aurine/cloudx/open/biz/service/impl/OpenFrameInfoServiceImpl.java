package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenFrameInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.FrameInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.aurine.cloudx.open.origin.service.ProjectFrameInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenFrameInfoServiceImpl implements OpenFrameInfoService {

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;


    @Override
    public R<FrameInfoVo> getById(String id) {
        ProjectFrameInfo po = projectFrameInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        FrameInfoVo vo = new FrameInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<FrameInfoVo>> page(Page page, FrameInfoVo vo) {
        return R.ok(projectFrameInfoService.page(page, vo));
    }

    @Override
    public R<FrameInfoVo> save(FrameInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<FrameInfoVo> update(FrameInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
