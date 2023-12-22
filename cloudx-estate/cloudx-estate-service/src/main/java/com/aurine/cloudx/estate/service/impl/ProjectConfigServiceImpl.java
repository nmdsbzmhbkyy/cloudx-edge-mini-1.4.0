package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.util.web.ContextHolderUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectConfig;
import com.aurine.cloudx.estate.mapper.ProjectConfigMapper;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
@Service
public class ProjectConfigServiceImpl extends ServiceImpl<ProjectConfigMapper, ProjectConfig> implements ProjectConfigService {

    @Override
    public ProjectConfig getConfig() {
        ProjectConfig projectConfig = super.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId()));
        if (projectConfig == null) {
            baseMapper.insert(new ProjectConfig());
        }
        return super.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId()));
    }

    @Override
    public void initDefaultData(Integer projectId, Integer tenantId) {
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setProjectId(projectId);
        projectConfig.setVisitorAudit("1");
        projectConfig.setAlarmTimeLimit(30);
        projectConfig.setAuthAudit("2");
        projectConfig.setServiceInitalStatus("1");
        projectConfig.setOperator(SecurityUtils.getUser().getId());
        baseMapper.init(projectConfig);
    }
}
