package com.aurine.cloudx.estate.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectAttendancePoint;

/**
 * 考勤点设置(ProjectAttendancePoint)表数据库访问层
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
@Mapper
public interface ProjectAttendancePointMapper extends BaseMapper<ProjectAttendancePoint> {

}
