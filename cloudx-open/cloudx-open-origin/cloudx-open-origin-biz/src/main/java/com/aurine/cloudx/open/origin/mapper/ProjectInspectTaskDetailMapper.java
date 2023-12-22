package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectInspectTaskDetailVo;
import com.aurine.cloudx.open.origin.entity.ProjectInspectTaskDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备巡检任务明细(ProjectInspectTaskDetail)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:59
 */
@Mapper
public interface ProjectInspectTaskDetailMapper extends BaseMapper<ProjectInspectTaskDetail> {

    /**
     * <p>
     * 根据任务id获取对应的巡检任务明细列表（巡检点）
     * </p>
     *
     * @param taskId 巡检任务ID
     * @return 巡检任务明细vo对象列表
     */
    List<ProjectInspectTaskDetailVo> listByTaskId(@Param("taskId") String taskId);

    /**
     * 获取签到类型
     * @param detailId
     * @return
     */
    String listCheckInDetailById(@Param("detailId") String detailId);
}