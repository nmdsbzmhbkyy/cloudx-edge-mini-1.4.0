package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectInspectPlanShift3Vo;
import com.aurine.cloudx.open.origin.entity.ProjectInspectPlanShift3;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:26
 */
public interface ProjectInspectPlanShift3Service extends IService<ProjectInspectPlanShift3> {

    /**
     * <p>
     * cron表达式类型是每天
     * </p>
     *
     * @param planId 计划id
     * @return 处理结果
     */
    boolean saveOrUpdateShiftByDay(Map<String, List<ProjectInspectPlanShift3Vo>> shift3VoMap, String planId);

    /**
     * <p>
     * cron表达式类型是周
     * </p>
     *
     * @param planId 计划id
     * @return 处理结果
     */
    boolean saveOrUpdateShiftByWeek(Map<String, List<ProjectInspectPlanShift3Vo>> projectInspectPlanShift3VoList, String planId);

    /**
     * <p>
     * cron表达式类型是自定义
     * </p>
     *
     * @param planId 计划id
     * @return 处理结果
     */
    boolean saveOrUpdateShiftByCustomer(Map<String, List<ProjectInspectPlanShift3Vo>> projectInspectPlanShift3VoList, String planId);

    /**
     * <p>
     * 根据计划id获取到班次map数据
     * </p>
     *
     * @param planId 计划id
     * @return 存有班次信息的map对象数据
     */
    Map<String, List<ProjectInspectPlanShift3Vo>> getShiftListMapByPlanId(String planId, String cornType);

    /**
     * <p>
     * 根据计划id删除本次数据
     * </p>
     *
     * @param planId 计划id
     * @return 处理结果
     */
    boolean removeShiftByPlanId(String planId);


    /**
     * <p>
     * 根据人员ID删除这个人员相关的巡检计划安排
     * </p>
     *
     * @author: 王良俊
     */
    boolean removePerson(String personId);

}