package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectNoticeCategoryConf;
import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.mapper.ProjectNoticeCategoryConfMapper;
import com.aurine.cloudx.estate.service.ProjectNoticeCategoryConfService;
import com.aurine.cloudx.estate.service.ProjectNoticeTemplateService;
import com.aurine.cloudx.estate.vo.ProjectNoticeCategoryConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)表服务实现类
 *
 * @author makejava
 * @since 2020-12-14 10:06:44
 */
@Service
public class ProjectNoticeCategoryConfServiceImpl extends ServiceImpl<ProjectNoticeCategoryConfMapper, ProjectNoticeCategoryConf> implements ProjectNoticeCategoryConfService {

    @Resource
    ProjectNoticeTemplateService projectNoticeTemplateService;

    @Override
    public IPage<ProjectNoticeCategoryConfVo> pageVo(Page<ProjectNoticeCategoryConfVo> page, ProjectNoticeCategoryConf projectNoticeCategoryConf) {
        return baseMapper.pageVo(page, projectNoticeCategoryConf);
    }

    @Override
    public boolean removeTypeById(String id) {
        //添加模板类型删除校验
        ProjectNoticeCategoryConf projectNoticeCategoryConf = getById(id);
        Integer count = projectNoticeTemplateService.countByTypeId(projectNoticeCategoryConf.getTypeId());
        if (count != null && count > 0) {
            throw new CheckedException("无法删除，该类型已被模板使用");
        }
        return removeById(id);
    }

    @Override
    public List<ProjectNoticeCategoryConf> getVoListByTypeName(String typeName) {
        return baseMapper.getVoListByTypeName(typeName);
    }
}