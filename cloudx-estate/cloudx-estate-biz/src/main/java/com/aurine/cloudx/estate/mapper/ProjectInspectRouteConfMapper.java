package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectInspectRouteConf;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectRouteConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备巡检路线设置(ProjectInspectRouteConf)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-23 18:33:19
 */
@Mapper
public interface ProjectInspectRouteConfMapper extends BaseMapper<ProjectInspectRouteConf> {

    /**
     * <p>
     * 获取巡检路线分页数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectRouteConfVo> fetchList(Page<ProjectInspectRouteConfVo> page, @Param("query") ProjectInspectRouteConfSearchConditionVo query);

}