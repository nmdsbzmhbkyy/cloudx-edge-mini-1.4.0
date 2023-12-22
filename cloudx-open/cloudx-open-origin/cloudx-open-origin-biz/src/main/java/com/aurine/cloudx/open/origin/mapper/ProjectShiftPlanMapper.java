package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectShiftPlan;
import com.aurine.cloudx.open.origin.vo.ProjectShiftPlanPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 排班计划(ProjectShiftPlan)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 10:48:33
 */
@Mapper
public interface ProjectShiftPlanMapper extends BaseMapper<ProjectShiftPlan> {

    /**
     * 排班计划分页查询
     *
     * @param page
     * @param projectShiftPlanPageVo
     * @return
     */
    Page<ProjectShiftPlanPageVo> pageShiftPlan(Page<ProjectShiftPlanPageVo> page, @Param("query") ProjectShiftPlanPageVo projectShiftPlanPageVo);
}