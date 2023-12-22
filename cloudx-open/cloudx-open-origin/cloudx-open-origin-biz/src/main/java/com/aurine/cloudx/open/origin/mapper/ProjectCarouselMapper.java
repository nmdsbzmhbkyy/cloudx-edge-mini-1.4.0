package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectCarouselVo;
import com.aurine.cloudx.open.origin.entity.ProjectCarousel;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    Page<ProjectCarouselVo> pageVo(Page page, @Param("query") ProjectCarouselVo projectCarouselVo);

    /**
     * 查询轮播图列表
     * @param type
     * @return
     */
    List<ProjectCarouselVo> listVo(@Param("type") String type);

    /**
     * 初始化轮播图数据
     * @param projectCarousels
     */
    @SqlParser(filter=true)
    void init(@Param("list") List<ProjectCarousel> projectCarousels, @Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);
}
