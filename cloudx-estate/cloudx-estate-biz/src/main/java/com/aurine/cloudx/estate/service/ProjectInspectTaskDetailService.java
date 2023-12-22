package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskDetailVo;
import com.aurine.cloudx.estate.vo.ProjectInspectVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备巡检任务明细(ProjectInspectTaskDetail)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:59
 */
public interface ProjectInspectTaskDetailService extends IService<ProjectInspectTaskDetail> {

    /**
     * <p>
     * 初始化任务明细数据
     * </p>
     *
     * @param taskList 巡检任务对象列表
     * @return 处理结果
     */
    boolean initTaskDetails(List<ProjectInspectTask> taskList);

    /**
     * <p>
     * 根据ID删除巡检任务明细
     * </p>
     *
     * @param taskId 任务id
     * @return 处理结果
     */
    boolean removeDetailByTaskId(String taskId);

    /**
     * <p>
     * 根据任务id获取任务明细列表（检查点）
     * </p>
     *
     * @param taskId 任务id
     * @return 巡检任务明细对象列表
     */
    List<ProjectInspectTaskDetailVo> listByTaskId(String taskId);

    void saveInspectTaskDetail(ProjectInspectTaskDetail taskDetail);

    /**
     * 巡检签到接口
     * @param taskVo
     */
    void sign(ProjectInspectVo taskVo);

    List<ProjectInspectCheckinDetail> listCheckInDetailById(String detailId);
}