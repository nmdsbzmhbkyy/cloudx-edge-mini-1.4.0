package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskPageVo;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectTaskVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检任务(ProjectInspectTask)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:50
 */
@Mapper
public interface ProjectInspectTaskMapper extends BaseMapper<ProjectInspectTask> {

    /**
     * <p>
     * 获取巡检任务分页数据
     * </p>
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 分页数据
     */
    Page<ProjectInspectTaskPageVo> fetchList(Page page, @Param("query") ProjectInspectTaskSearchConditionVo query);

    /**
     * <p>
     * 获取到今天可以进行任务分配的任务对象列表
     * </p>
     *
     * @return 巡检任务vo对象列表（符合条件的）
     */
    List<ProjectInspectTaskVo> listAllPlanTask();

    /**
     * <p>
     * 这里传入的数据都必须先做非空判断
     * 获取到传入时间之前的创建的任务列表
     * </p>
     *
     * @param date   日期 例如 2020-01-01
     * @param planId 计划id
     * @return 巡检任务对象列表
     */
    List<ProjectInspectTask> listByLastWeek(@Param("date") String date, @Param("planId") String planId);

    /**
     * <p>
     * 获取到所有已经超时的任务id列表
     * </p>
     *
     * @return 已超时但未设置为超时的任务id数组
     */
    void updateAllTimeOut();

    /**
     * <p>
     * 获取到所有已经超时的任务id列表
     * </p>
     *
     * @return 已超时但未设置为超时的任务id数组
     */
    boolean saveTaskBatch(List<ProjectInspectTask> taskList);

    /**
     * <p>
     * 获取可用的任务编码
     * </p>
     *
     * @return 返回可用的任务编码
     */
    String getTaskCode(@Param("projectId") Integer projectId);

    Page<ProjectInspectTaskPageVo> selectForMe(Page page, @Param("staffId") String staffId);

    Page<ProjectInspectTaskPageVo> selectToDo(Page page, @Param("staffId") String staffId);

    Integer getCount(@Param("staffId") String staffId, @Param("status") String status, @Param("date") String date);

    Page<ProjectInspectTaskPageVo> selectDateToDo(Page page, @Param("staffId")String staffId, @Param("date")String date);
}