package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectPatrolInfoSearchCondition;
import com.aurine.cloudx.open.origin.entity.ProjectPatrolInfo;
import com.aurine.cloudx.open.origin.vo.ProjectPatrolInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目巡更记录(ProjectPatrolInfo)表数据库访问层
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 09:01:23
 */
@Mapper
public interface ProjectPatrolInfoMapper extends BaseMapper<ProjectPatrolInfo> {

    IPage<ProjectPatrolInfoVo> page(Page<ProjectPatrolInfoVo> page, @Param("param") ProjectPatrolInfoSearchCondition vo);

    ProjectPatrolInfoVo selectByVoId(@Param("patrolId") String patrolId);

    IPage<ProjectPatrolInfoVo> pageByPatrolInfoToDo(Page<ProjectPatrolInfoVo> page, @Param("staffId") String staffId, @Param("param") ProjectPatrolInfoSearchCondition vo);

    Page<ProjectPatrolInfoVo> selectDateToDo(Page page, @Param("staffId") String staffId, @Param("date") String date);

    IPage<ProjectPatrolInfoVo> pageByPatrolInfoForMe(Page<ProjectPatrolInfoVo> page, @Param("staffId") String staffId, @Param("param") ProjectPatrolInfoSearchCondition vo);

    /**
     * 将过期未开始巡更的路线设置为超时
     */
    void updateAllTimeOut();

    /**
     * 统计任务
     */
    Integer getCount(@Param("staffId") String staffId, @Param("status") String status, @Param("date") String date);
}