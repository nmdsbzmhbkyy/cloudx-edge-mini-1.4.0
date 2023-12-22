
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DefaultLabelEnum;
import com.aurine.cloudx.estate.entity.ProjectLabelConfig;
import com.aurine.cloudx.estate.mapper.ProjectLabelConfigMapper;
import com.aurine.cloudx.estate.service.ProjectLabelConfigService;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 标签管理
 *
 * @author 王伟
 * @date 2020-05-07 08:09:35
 */
@Service
@AllArgsConstructor
public class ProjectLabelConfigServiceImpl extends ServiceImpl<ProjectLabelConfigMapper, ProjectLabelConfig>
        implements ProjectLabelConfigService {

    private final ProjectLabelConfigMapper projectLabelConfigMapper;

    @Override
    public IPage<ProjectLabelConfig> findPage(IPage<ProjectLabelConfig> page, String labelName) {
        return projectLabelConfigMapper.select(page,labelName);
    }

    @Override
    public boolean saveLabel(ProjectLabelConfig projectLabelConfig) {
//        projectLabelConfig.setProjectId(ProjectContextHolder.getProjectId());
//        projectLabelConfig.setLabelCode("");

        projectLabelConfig.setLabelId(UUID.randomUUID().toString().replaceAll("-", ""));
        projectLabelConfig.setUseCount(0);
        return this.save(projectLabelConfig);
//        return projectLabelConfigMapper.initInsert(projectLabelConfig, ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
    }

    /**
     * 初始化标签
     * @return
     */
    @Override
    public boolean initLabel(Integer projectId,Integer tenantId) {
        //判断默认标签是否存在
        List<ProjectLabelConfig> defaultLabel = projectLabelConfigMapper.selectByDefault(
                ProjectContextHolder.getProjectId(), DefaultLabelEnum.OLD_MAN.code, DefaultLabelEnum.ARREARS.code, DefaultLabelEnum.DIFFICULTY.code
        );
        if (CollUtil.isNotEmpty(defaultLabel)) {
            return false;
        }
        List<ProjectLabelConfig> result = projectLabelConfigMapper.selectByTemplate();
        for (ProjectLabelConfig po : result) {
            po.setLabelId(UUID.randomUUID().toString().replace("-", ""));
            projectLabelConfigMapper.initInsert(po, projectId, tenantId);
        }
        return true;
    }
}
