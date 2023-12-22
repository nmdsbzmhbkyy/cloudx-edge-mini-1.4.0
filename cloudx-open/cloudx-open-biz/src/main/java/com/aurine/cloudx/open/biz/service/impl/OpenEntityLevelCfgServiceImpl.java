package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenEntityLevelCfgService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.EntityLevelCfgVo;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.open.origin.service.ProjectEntityLevelCfgService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

@Service
public class OpenEntityLevelCfgServiceImpl implements OpenEntityLevelCfgService {

    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;


    @Override
    public R<EntityLevelCfgVo> getById(String id) {
        ProjectEntityLevelCfg po = projectEntityLevelCfgService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        EntityLevelCfgVo vo = new EntityLevelCfgVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<EntityLevelCfgVo>> page(Page page, EntityLevelCfgVo vo) {
        return R.ok();
    }

    @Override
    public R<EntityLevelCfgVo> save(EntityLevelCfgVo vo) {
        return R.ok();
    }

    @Override
    public R<EntityLevelCfgVo> update(EntityLevelCfgVo vo) {
        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
        return R.ok();
    }

}
