package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 轮播图资讯管理(ProjectCarouseInfoconf)表服务接口
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:03
 */
public interface ProjectCarouselConfService extends IService<ProjectCarouselConf> {

    Page<ProjectCarouselConfVo> fetchList(Page<ProjectCarouselConf> page, ProjectCarouselConfQuery carouselConfQuery);
}