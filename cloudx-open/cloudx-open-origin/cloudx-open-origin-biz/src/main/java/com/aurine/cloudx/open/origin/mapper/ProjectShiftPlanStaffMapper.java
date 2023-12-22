package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectShiftPlanStaff;
import com.aurine.cloudx.open.origin.vo.ProjectStaffListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 记录排班计划关联的参与人信息(ProjectShiftPlanStaff)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 11:01:03
 */
@Mapper
public interface ProjectShiftPlanStaffMapper extends BaseMapper<ProjectShiftPlanStaff> {

    /**
     * 根据计划id查询员工列表
     *
     * @param planId
     * @return
     */
    List<ProjectStaffListVo> getStaffListByPlanId(@Param("planId") String planId);
}