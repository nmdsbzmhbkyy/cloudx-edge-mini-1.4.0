package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.vo.ProjectAttendanceCountPageVo;
import com.aurine.cloudx.estate.vo.ProjectAttendanceVo;
import com.aurine.cloudx.estate.vo.ProjectCountAttendanceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeptCountAttendanceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 考勤打卡(ProjectAttendancePoint)表服务接口
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
public interface ProjectAttendanceService extends IService<ProjectAttendance> {
    /**
     * 获取员工打卡信息
     * @param lat
     * @param lon
     * @return
     */
    ProjectAttendanceVo getAttendanceInfo (Double lat, Double lon);

    /**
     * 员工上下班打卡
     * @param attendanceId
     * @param lat
     * @param lon
     * @return
     */
    R clockIn (String attendanceId, Double lat, Double lon);

    /**
     * 根据员工ID、date 统计考勤打卡
     * @param staffId
     * @param date
     * @return
     */
    ProjectCountAttendanceInfoVo countAttendanceInfo (String staffId, LocalDate date);
    /**
     * 根据deptId、date 分页统计部门下所有员工的考勤打卡
     * @param deptId
     * @param date
     * @return
     */
    Page<ProjectAttendanceCountPageVo> countDeptAttendancePage (Page page, Integer deptId, String date);

    /**
     * 根据deptId、date 汇总部门下所有员工的考勤打卡
     * @param deptId
     * @param date
     * @return
     */
    ProjectDeptCountAttendanceVo countDeptAttendance (Integer deptId, String date);
}
