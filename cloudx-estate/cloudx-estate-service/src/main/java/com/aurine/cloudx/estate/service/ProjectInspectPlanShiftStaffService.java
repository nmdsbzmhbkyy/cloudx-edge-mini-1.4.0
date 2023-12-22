package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectPlanShiftStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡检计划班次执行人员列表(ProjectInspectPlanShiftStaff)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-27 10:37:23
 */
public interface ProjectInspectPlanShiftStaffService extends IService<ProjectInspectPlanShiftStaff> {

    /**
     * <p>
     * 保存班次和执行人员之间的关系
     * </p>
     *
     * @param staffIdArr 执行人id数组
     * @param recordId   班次id
     * @return 处理结果
     */
    boolean saveOrUpdateBatchShiftStaffRel(String[] staffIdArr, String recordId);

    /**
     * <p>
     * 根据班次id获取到这个班次执行人的id数组
     * </p>
     *
     * @param recordId 班次id
     * @return 班次执行人id数组
     */
    String[] getStaffIdArrByShiftId(String recordId);

    /**
     * <p>
     * 根据班次id列表删除所有班次的人员关系
     * </p>
     *
     * @param recordIdList 班次id列表
     * @return 处理结果
     */
    boolean removeRel(List<String> recordIdList);

}