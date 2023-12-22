package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateForm;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 推送中心消息模板配置(ProjectNoticeTemplate)表服务接口
 *
 * @author makejava
 * @since 2020-12-14 09:34:22
 */
public interface ProjectNoticeTemplateService extends IService<ProjectNoticeTemplate> {

    IPage<ProjectNoticeTemplateVo> pageVo(IPage page, ProjectNoticeTemplateForm projectNoticeTemplate);


    ProjectNoticeTemplate getVoById(String id);

    boolean updateActiveById(String id, String isActive);

    Integer countByTypeId(String typeId);

    List<ProjectNoticeTemplate> getVoBytitle(ProjectNoticeTemplateVo projectNoticeTemplateVo);


}