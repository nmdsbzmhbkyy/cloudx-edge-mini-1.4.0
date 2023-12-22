package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskAndDetailVo;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskPageVo;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectStaffWorkVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备巡检任务(ProjectInspectTask)表服务接口
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
public interface ProjectInspectTaskService extends IService<ProjectInspectTask> {

    /**
     * <p>
     * 查询分页数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectTaskPageVo> fetchList(Page page, ProjectInspectTaskSearchConditionVo query);

    /**
     * <p>
     * 取消任务
     * </p>
     *
     * @param taskId 任务id
     * @return 处理结果
     */
    boolean cancel(String taskId);

    /**
     * <p>
     * 初始化任务
     * </p>
     *
     * @return 处理结果
     */
    boolean initTask();

    /**
     * <p>
     * 删除任务
     * </p>
     *
     * @param taskId 任务id
     * @return 处理结果
     */
    boolean removeTaskById(String taskId);

    /**
     * <p>
     * 处理所有超时的任务
     * </p>
     *
     * @return 处理结果
     */
    boolean dealTimeOut();

    /**
     * <p>
     * 完成任务
     * </p>
     *
     * @param taskId 任务ID
     */
    void completeTask(String taskId);

    /**
     * <p>
     * 任务领取
     * </p>
     *
     * @param taskId  所要领取的任务ID
     * @param staffId 要领取任务的员工ID
     * @return 是否领取成功
     */
    void claimTask(String taskId, String staffId);

    /**
     * 分页查询待我认领的任务（微信相关）
     *
     * @param page
     * @return
     */
    Page<ProjectInspectTaskPageVo> selectForMe(Page page, String staffId);

    /**
     * 分页查询待我执行的任务（微信相关）
     *
     * @param page
     * @return
     */
    Page<ProjectInspectTaskPageVo> selectToDo(Page page, String staffId);

    /**
     * 分页查询待我完成的任务（微信相关）
     *
     * @param page
     * @return
     */
    Page<ProjectInspectTaskPageVo> selectDateToDo(Page page, String staffId,String date);
    /**
     * 获取巡检任务及明细列表
     *
     * @param taskId
     * @return
     */
    ProjectInspectTaskAndDetailVo getTaskAndDetailById(String taskId);

    /**
     * 统计任务数微信相关）
     * @param staffId
     * @param date
     * @return
     */
    ProjectStaffWorkVo getCount (String staffId, String date);
}