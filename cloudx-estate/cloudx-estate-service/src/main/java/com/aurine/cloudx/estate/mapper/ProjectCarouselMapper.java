package com.aurine.cloudx.estate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * 轮播图管理
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/12 15:30
 */
@Mapper
public interface ProjectCarouselMapper extends BaseMapper<ProjectCarousel> {
    
    /**
     * 分页查询轮播图
     *
     * @param page
     * @param projectCarouselVo
     * @return
     */
    Page<ProjectCarouselVo> pageVo(Page page,@Param("query") ProjectCarouselVo projectCarouselVo);

    /**
     * 查询轮播图列表
     * @param type
     * @return
     */
    List<ProjectCarouselVo> listVo(@Param("type") String type);
}
