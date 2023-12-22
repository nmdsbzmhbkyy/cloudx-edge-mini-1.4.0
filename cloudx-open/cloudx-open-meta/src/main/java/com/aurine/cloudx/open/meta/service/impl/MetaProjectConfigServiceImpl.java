package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaProjectConfigService;
import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.aurine.cloudx.open.origin.service.ProjectConfigService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 项目参数设置
 *
 * @author : zy
 * @date : 2022-11-24 09:50:33
 */
@Service
public class MetaProjectConfigServiceImpl implements MetaProjectConfigService {


    @Resource
    private ProjectConfigService projectConfigService;


    @Override
    public R<ProjectConfig> save(ProjectConfig po) {
        //boolean result = projectConfigService.saveOrUpdate(po);
        boolean result = saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectConfig> update(ProjectConfig po) {
        boolean result = saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(po);
    }

    private boolean saveOrUpdate(ProjectConfig po) {
        ProjectConfig projectConfig = projectConfigService.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, po.getProjectId()).last("LIMIT 1"));
        boolean result;
        if(projectConfig == null){
            result = projectConfigService.save(po);
        }else {
            result = projectConfigService.update(po, new UpdateWrapper<ProjectConfig>().lambda().eq(ProjectConfig::getProjectId, po.getProjectId()));

        }
        return result;
    }

    @Override
    public R<Boolean> delete(String id) {
        boolean result = projectConfigService.removeById(id);
        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
