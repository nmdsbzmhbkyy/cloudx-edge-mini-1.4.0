package com.aurine.cloudx.estate.service;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.vo.ProjectStaffListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectShiftPlanStaff;

import java.util.List;

/**
 * 记录排班计划关联的参与人信息(ProjectShiftPlanStaff)表服务接口
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 11:01:03
 */
public interface ProjectShiftPlanStaffService extends IService<ProjectShiftPlanStaff> {

    /**
     * 根据排班计划id获取该排班计划下的所有员工id
     *
     * @param planId
     * @return
     */
    List<String> getByPlanId(String planId);

    /**
     * 根据排班id查询员工列表
     * @param planId
     * @return
     */
    List<ProjectStaffListVo> getStaffListByPlanId(String planId);
}