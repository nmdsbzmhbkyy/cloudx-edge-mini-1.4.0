package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 轮播图管理
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 15:21
 */
public interface ProjectCarouselService extends IService<ProjectCarousel> {
    
    
    
    
    /**
     * 分页查询轮播图
     *
     * @param page
     * @param projectCarouselVo
     * @return
     */
    Page<ProjectCarouselVo> pageVo(Page page, ProjectCarouselVo projectCarouselVo);

    List<ProjectCarouselVo> listVo(String type);

    List<ProjectCarouselVo> listByType(String type);

    void upObj(String id);

    void downObj(String id);

    void init(Integer projectId, Integer tenantId);
}
