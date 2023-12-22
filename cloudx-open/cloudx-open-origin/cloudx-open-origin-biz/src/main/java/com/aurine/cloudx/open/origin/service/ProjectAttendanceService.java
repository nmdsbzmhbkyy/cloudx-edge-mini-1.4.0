package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectAttdanceSummaryVo;
import com.aurine.cloudx.open.origin.entity.ProjectAttendance;
import com.aurine.cloudx.open.origin.vo.ProjectAttendanceQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 考勤打卡(ProjectAttendancePoint)表服务接口
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
public interface ProjectAttendanceService extends IService<ProjectAttendance> {
    IPage<ProjectAttendanceQueryVo> page(Page page, ProjectAttendanceQueryVo query);

    List<ProjectAttendanceQueryVo> getProjectAttendanceList(ProjectAttendanceQueryVo query);

//    void exportExcel(HttpServletResponse httpServletResponse, ProjectAttendanceQueryVo projectAttendanceQueryVo);

    IPage<ProjectAttdanceSummaryVo> attendanceSummaryPage(Page page, ProjectAttdanceSummaryVo query);

    List <ProjectAttdanceSummaryVo>listCount(ProjectAttdanceSummaryVo query);

//    void exportAttendanceSummaryExcel(HttpServletResponse httpServletResponse, ProjectAttdanceSummaryVo projectAttdanceSummaryVo);


}
