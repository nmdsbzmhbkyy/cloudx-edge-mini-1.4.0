package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-04 10:08:52
 */
@Mapper
public interface ProjectInspectCheckinDetailMapper extends BaseMapper<ProjectInspectCheckinDetail> {

    /**
     * <p>
     *  根据任务ID获取这个任务未签到的巡检点数
     * </p>
     *
     * @param taskId 任务ID
     * @return 未签到巡检点数
    */
    int countUnCheckin(String taskId);
}