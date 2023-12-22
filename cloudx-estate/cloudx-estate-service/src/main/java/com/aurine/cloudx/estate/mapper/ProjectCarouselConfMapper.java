package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 轮播图资讯管理(ProjectCarouseInfoconf)表数据库访问层
 *
 * @author 王良俊
 * @since 2021-01-12 11:43:11
 */
@Mapper
public interface ProjectCarouselConfMapper extends BaseMapper<ProjectCarouselConf> {

    /**
     * <p>
     *  获取资讯的分页数据
     * </p>
     *
     * @param carouselConfQuery 查询条件
     * @param page 分页数据
    */
    Page<ProjectCarouselConfVo> fetchList(Page<ProjectCarouselConf> page, @Param("query") ProjectCarouselConfQuery carouselConfQuery);
}