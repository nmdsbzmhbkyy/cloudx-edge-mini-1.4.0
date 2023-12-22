package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPatrolRouteConf;
import com.aurine.cloudx.estate.vo.ProjectPatrolRouteConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 巡更路线配置
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @date 2020-05-07 09:13:25
 */
@Mapper
public interface ProjectPatrolRouteConfMapper extends BaseMapper<ProjectPatrolRouteConf> {

    IPage<ProjectPatrolRouteConfVo> selectAllList(Page page, @Param("param") ProjectPatrolRouteConfVo vo);

    ProjectPatrolRouteConfVo select(@Param("routeId") String patrolRouteId, @Param("projectId") Integer projectId);

}
