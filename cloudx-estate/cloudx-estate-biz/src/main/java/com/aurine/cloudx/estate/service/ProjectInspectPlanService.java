package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectPlan;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:05
 */
public interface ProjectInspectPlanService extends IService<ProjectInspectPlan> {

    /**
     * <p>
     * 获取计划的分页数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectPlanVo> fetchList(Page page, ProjectInspectPlanSearchConditionVo query);

    /**
     * <p>
     * 保存或更新巡检计划
     * </p>
     *
     * @param projectInspectPlanVo 巡检计划vo对象
     * @return 处理结果
     */
    boolean saveOrUpdatePlan(ProjectInspectPlanVo projectInspectPlanVo);

    /**
     * <p>
     * 根据计划id获取到计划
     * </p>
     *
     * @param planId 巡检计划id
     * @return 巡检计划vo对象
     */
    ProjectInspectPlanVo getPlanById(String planId);

    /**
     * <p>
     * 删除计划
     * </p>
     *
     * @param planId 巡检计划id
     * @return 处理结果
     */
    boolean removePlan(String planId);

    /**
     * <p>
     * 启用或禁用计划
     * </p>
     *
     * @param planId 计划id
     * @param status 计划状态
     * @return 处理结果
     */
    boolean changeStatus(String planId, char status);

    /**
     * <p>
     * 将所有已经过时的计划状态设置为已结束
     * </p>
     *
     * @return 处理结果
     */
    boolean dealOutdatedPlan();

    /**
     * <p>
     * 判断巡检路线是否有计划在使用中
     * </p>
     *
     * @return 判断结果 true 为被使用
     */
    boolean routeIsUsed(String inspectRouteId);

}