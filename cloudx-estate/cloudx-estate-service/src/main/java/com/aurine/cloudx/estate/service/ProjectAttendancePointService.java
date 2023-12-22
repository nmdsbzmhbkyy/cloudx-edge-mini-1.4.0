package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectAttendancePoint;
import com.aurine.cloudx.estate.vo.ProjectAttendancePointForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考勤点设置(ProjectAttendancePoint)表服务接口
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
public interface ProjectAttendancePointService extends IService<ProjectAttendancePoint> {

    Boolean addBatch(ProjectAttendancePointForm form);

    ProjectAttendancePoint getAttendancePointInfo (Double lat, Double lon);
}
