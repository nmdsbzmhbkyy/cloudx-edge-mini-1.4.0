package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaLabelConfigService;
import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import com.aurine.cloudx.open.origin.service.ProjectLabelConfigService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-人员标签实现
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@Service
public class MetaLabelConfigServiceImpl implements MetaLabelConfigService {

    @Resource
    private ProjectLabelConfigService projectLabelConfigService;


    /**
     * 新增
     *
     * @param po
     * @return
     */
    @Override
    public R<ProjectLabelConfig> save(ProjectLabelConfig po) {
        boolean result = projectLabelConfigService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    /**
     * 修改
     *
     * @param po
     * @return
     */
    @Override
    public R<ProjectLabelConfig> update(ProjectLabelConfig po) {
        boolean result = projectLabelConfigService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectLabelConfigService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
