package com.aurine.cloudx.estate.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectShiftPlanStaff;

/**
 * 记录排班计划关联的参与人信息(ProjectShiftPlanStaff)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 11:01:03
 */
@Mapper
public interface ProjectShiftPlanStaffMapper extends BaseMapper<ProjectShiftPlanStaff> {

}