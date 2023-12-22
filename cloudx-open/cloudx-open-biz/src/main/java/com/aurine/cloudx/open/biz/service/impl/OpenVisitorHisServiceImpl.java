package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenVisitorHisService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.VisitorHisVo;
import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
import com.aurine.cloudx.open.origin.service.ProjectVisitorHisService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenVisitorHisServiceImpl implements OpenVisitorHisService {

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;


    @Override
    public R<VisitorHisVo> getById(String id) {
        ProjectVisitorHis po = projectVisitorHisService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        VisitorHisVo vo = new VisitorHisVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<VisitorHisVo>> page(Page page, VisitorHisVo vo) {
        return R.ok(projectVisitorHisService.page(page, vo));
    }

    @Override
    public R<VisitorHisVo> save(VisitorHisVo vo) {
        return R.ok();
    }

    @Override
    public R<VisitorHisVo> update(VisitorHisVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
