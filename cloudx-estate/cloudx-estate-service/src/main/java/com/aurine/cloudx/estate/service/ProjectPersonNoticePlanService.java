package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanFormVo;
import com.aurine.cloudx.estate.vo.ProjectPersonNoticePlanPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * 住户通知计划(ProjectPersonNoticePlan)表服务接口
 *
 * @author makejava
 * @since 2020-12-14 15:58:04
 */
public interface ProjectPersonNoticePlanService extends IService<ProjectPersonNoticePlan> {

    /**
     * 新增住户通知计划
     *
     * @param projectPersonNoticePlanFormVo
     * @return
     */
    Boolean saveNoticePlan(ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo);

    /**
     * 修改住户通知计划
     *
     * @param projectPersonNoticePlanFormVo
     * @return
     */
    Boolean updateNoticePlan(ProjectPersonNoticePlanFormVo projectPersonNoticePlanFormVo);

    /**
     * 分页查询住户通知计划
     *
     * @param page
     * @param projectPersonNoticePlanPageVo
     * @return
     */
    Page<ProjectPersonNoticePlanPageVo> pageNoticePlan(Page page, @Param("query") ProjectPersonNoticePlanPageVo projectPersonNoticePlanPageVo);

    /**
     * 通过id查询住户通知计划
     *
     * @param planId
     * @return
     */
    ProjectPersonNoticePlanFormVo getByPlanId(String planId);

    /**
     * 删除住户通知计划
     *
     * @param planId
     * @return
     */
    Boolean removeByPlanId(String planId);

    /**
     * 执行住户通知计划
     */
    void executeNoticePlan();
}