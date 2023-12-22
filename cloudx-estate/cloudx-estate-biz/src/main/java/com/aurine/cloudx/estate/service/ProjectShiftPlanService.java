package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectShiftPlan;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanFromVo;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 排班计划(ProjectShiftPlan)表服务接口
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 10:48:33
 */
public interface ProjectShiftPlanService extends IService<ProjectShiftPlan> {

    /**
     * 排班计划分页查询
     *
     * @param page
     * @param projectShiftPlanPageVo
     * @return
     */
    Page<ProjectShiftPlanPageVo> pageShiftPlan(Page<ProjectShiftPlanPageVo> page, @Param("query") ProjectShiftPlanPageVo projectShiftPlanPageVo);

    /**
     * 根据id获取排班计划
     *
     * @param planId
     * @return
     */
    ProjectShiftPlanFromVo getByPlanId(String planId);
    /**
     * 新增排班计划
     *
     * @param projectShiftPlanFromVo
     * @return
     */
    Boolean saveShiftPlan(ProjectShiftPlanFromVo projectShiftPlanFromVo);

    /**
     * 更新排班计划
     *
     * @param projectShiftPlanFromVo
     * @return
     */
    Boolean updateShiftPlan(ProjectShiftPlanFromVo projectShiftPlanFromVo);

    /**
     * 根据id删除排班计划
     * @param planId
     * @return
     */
    Boolean deleteByPlanId(String planId);

    /**
     * 根据计划名称获取排班计划
     *
     * @param planName
     * @param planId
     * @return
     */
    ProjectShiftPlan getByPlanName(String planName, String planId);

    /**
     * 查询今年的节假日安排
     * @return
     */
    List<String> listVacations();

    /**
     * 每年节假日更新时，更新节假日安排
     */
    void refreshHoliday();
}