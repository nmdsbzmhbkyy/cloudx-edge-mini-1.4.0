package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenVisitorInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.VisitorInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectVisitor;
import com.aurine.cloudx.open.origin.service.ProjectVisitorService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenVisitorInfoServiceImpl implements OpenVisitorInfoService {

    @Resource
    private ProjectVisitorService projectVisitorService;


    @Override
    public R<VisitorInfoVo> getById(String id) {
        ProjectVisitor po = projectVisitorService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        VisitorInfoVo vo = new VisitorInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<VisitorInfoVo>> page(Page page, VisitorInfoVo vo) {
        return R.ok(projectVisitorService.page(page, vo));
    }

    @Override
    public R<VisitorInfoVo> save(VisitorInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<VisitorInfoVo> update(VisitorInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
