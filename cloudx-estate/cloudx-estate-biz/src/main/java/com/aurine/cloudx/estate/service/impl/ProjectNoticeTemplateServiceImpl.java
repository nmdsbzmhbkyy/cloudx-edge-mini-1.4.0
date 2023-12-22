package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.mapper.ProjectNoticeTemplateMapper;
import com.aurine.cloudx.estate.service.ProjectNoticeTemplateService;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateForm;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推送中心消息模板配置(ProjectNoticeTemplate)表服务实现类
 *
 * @author makejava
 * @since 2020-12-14 09:34:22
 */
@Service
@Slf4j
public class ProjectNoticeTemplateServiceImpl extends ServiceImpl<ProjectNoticeTemplateMapper, ProjectNoticeTemplate> implements ProjectNoticeTemplateService {

    @Override
    public IPage<ProjectNoticeTemplateVo> pageVo(IPage page, ProjectNoticeTemplateForm projectNoticeTemplate) {
        Integer projectId = ProjectContextHolder.getProjectId();
        projectNoticeTemplate.setProjectId(projectId);
        return baseMapper.pageVo(page, projectNoticeTemplate);
    }


    @Override
    public ProjectNoticeTemplate getVoById(String id) {
        return baseMapper.getVoById(id);
    }

    @Override
    public boolean updateActiveById(String id, String isActive) {
        return baseMapper.updateActiveById(id, isActive);
    }

    @Override
    public Integer countByTypeId(String typeId) {
        return baseMapper.countByTypeId(typeId);
    }

    @Override
    public List<ProjectNoticeTemplate> getVoBytitle(ProjectNoticeTemplateVo projectNoticeTemplateVo) {
        return baseMapper.getVoByTitle(projectNoticeTemplateVo);
    }
}