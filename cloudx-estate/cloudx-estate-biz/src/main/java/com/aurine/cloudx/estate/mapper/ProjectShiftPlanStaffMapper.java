package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.vo.ProjectStaffListVo;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectShiftPlanStaff;
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