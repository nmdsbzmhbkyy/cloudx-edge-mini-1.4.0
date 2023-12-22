package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectNoticeCategoryConf;
import com.aurine.cloudx.estate.vo.ProjectNoticeCategoryConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)表服务接口
 *
 * @author makejava
 * @since 2020-12-14 10:06:44
 */
public interface ProjectNoticeCategoryConfService extends IService<ProjectNoticeCategoryConf> {

    IPage<ProjectNoticeCategoryConfVo> pageVo(Page<ProjectNoticeCategoryConfVo> page, ProjectNoticeCategoryConf projectNoticeCategoryConf);

    boolean removeTypeById(String id);
}