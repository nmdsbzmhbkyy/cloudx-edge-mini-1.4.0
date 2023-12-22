package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenUnitInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectUnitInfo;
import com.aurine.cloudx.open.origin.service.ProjectUnitInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-单元信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenUnitInfoServiceImpl implements OpenUnitInfoService {

    @Resource
    private ProjectUnitInfoService projectUnitInfoService;


    @Override
    public R<UnitInfoVo> getById(String id) {
        ProjectUnitInfo po = projectUnitInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        UnitInfoVo vo = new UnitInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<UnitInfoVo>> page(Page page, UnitInfoVo vo) {
        return R.ok(projectUnitInfoService.page(page, vo));
    }

    @Override
    public R<UnitInfoVo> save(UnitInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<UnitInfoVo> update(UnitInfoVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
