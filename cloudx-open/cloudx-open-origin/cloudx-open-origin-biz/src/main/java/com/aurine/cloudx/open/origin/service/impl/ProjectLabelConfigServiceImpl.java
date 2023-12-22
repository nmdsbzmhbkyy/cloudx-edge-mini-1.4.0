
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.mapper.ProjectLabelConfigMapper;
import com.aurine.cloudx.open.origin.constant.enums.DefaultLabelEnum;
import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import com.aurine.cloudx.open.origin.vo.ProjectLabelConfigVo;
import com.aurine.cloudx.open.origin.service.ProjectLabelConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public IPage<ProjectLabelConfig> findPage(IPage<ProjectLabelConfig> page, ProjectLabelConfigVo projectLabelConfigVo) {
        projectLabelConfigVo.setProjectId(ProjectContextHolder.getProjectId());
        projectLabelConfigVo.setTenantId(TenantContextHolder.getTenantId());
        return projectLabelConfigMapper.select(page,projectLabelConfigVo);
    }

    @Override
    public boolean saveLabel(ProjectLabelConfig projectLabelConfig) {
//        projectLabelConfig.setProjectId(ProjectContextHolder.getProjectId());
//        projectLabelConfig.setLabelCode("");

        projectLabelConfig.setLabelId(UUID.randomUUID().toString().replaceAll("-", ""));
        projectLabelConfig.setUseCount(0);
        projectLabelConfig.setCreateTime(LocalDateTime.now());
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

    /**
     * @description: 根据ID获取标签
     * @param:  labelId
     * @return:
     * @author cjw
     * @date: 2021/3/19 11:11
     */
    @Override
    public ProjectLabelConfigVo selectByLabelId(String labelId) {
        return projectLabelConfigMapper.selectByLabelId(labelId);
    }

    @Override
    public List<ProjectLabelConfigVo> selectProjectTemplate(String labelName,Integer projectId) {
        return projectLabelConfigMapper.selectProjectTemplate(labelName,projectId);
    }
}
