package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.AttendanceStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.entity.ProjectAttendancePoint;
import com.aurine.cloudx.estate.mapper.ProjectAttendanceMapper;
import com.aurine.cloudx.estate.service.ProjectAttendancePointService;
import com.aurine.cloudx.estate.service.ProjectAttendanceService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.ProjectStaffShiftDetailService;
import com.aurine.cloudx.estate.util.GpsUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * 考勤打卡(ProjectAttendancePoint)表服务实现类
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
@Service
public class ProjectAttendanceServiceImpl extends ServiceImpl<ProjectAttendanceMapper, ProjectAttendance> implements ProjectAttendanceService {
    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private ProjectAttendancePointService projectAttendancePointService;

    @Resource
    private ProjectStaffShiftDetailService projectStaffShiftDetailService;

    @Override
    public ProjectAttendanceVo getAttendanceInfo(Double lat, Double lon) {
        ProjectStaffVo staffVo = projectStaffService.getStaffByOwner();
        LocalDate date = LocalDate.now();
        ProjectAttendance projectAttendance = this.getOne(new QueryWrapper<ProjectAttendance>().lambda()
                .eq(ProjectAttendance::getStaffId, staffVo.getStaffId()).eq(ProjectAttendance::getAttenDate, date));
        ProjectAttendanceVo attendanceVo = new ProjectAttendanceVo();
        // 当天员工打卡信息不存在，新增数据
        if (ObjectUtil.isEmpty(projectAttendance)) {
            projectAttendance = new ProjectAttendance();
            // 获取上下班时间

            ProjectAttendance workTime = projectStaffService.getWorkTime(staffVo.getStaffId(), date);

            if (AttendanceStatusEnum.REST.code.equals(workTime.getResult()) || AttendanceStatusEnum.NONE.code.equals(workTime.getResult())) {
                attendanceVo.setResult(workTime.getResult());
                return attendanceVo;
            }
            BeanUtil.copyProperties(workTime, projectAttendance);
            BeanUtil.copyProperties(staffVo, projectAttendance);
            projectAttendance.setAttenDate(date);
            projectAttendance.setDeptId(staffVo.getDepartmentId());
            projectAttendance.setDeptName(staffVo.getDeptName());
            // 默认考勤状态为旷工
            projectAttendance.setResult(AttendanceStatusEnum.ABSENTEEISM.code);
            this.save(projectAttendance);
        }
        BeanUtil.copyProperties(projectAttendance, attendanceVo);
        if (lat != null && lon != null) {
            ProjectAttendancePoint attendancePointInfo = projectAttendancePointService.getAttendancePointInfo(lat, lon);
            if (ObjectUtil.isNotEmpty(attendancePointInfo)) {
                attendanceVo.setArea(attendancePointInfo.getPointAddress());
            }
            attendanceVo.setIsClockIn(isClockIn(lat, lon));
        }
        return attendanceVo;
    }

    /**
     * 判断员工是否可以打卡
     *
     * @param lat
     * @param lon
     * @return
     */
    String isClockIn(Double lat, Double lon) {
        List<ProjectAttendancePoint> attendancePointList = projectAttendancePointService.list();
        for (ProjectAttendancePoint attendancePoint : attendancePointList) {
            if (GpsUtil.getDistance(lat, lon, attendancePoint.getLat(), attendancePoint.getLon()) < attendancePoint.getPointPrecision()) {
                return "1";
            }
        }
        return "0";
    }

    @Override
    public R clockIn(String attendanceId, Double lat, Double lon) {
        ProjectAttendancePoint attendancePointInfo = projectAttendancePointService.getAttendancePointInfo(lat, lon);
        if (ObjectUtil.isEmpty(attendancePointInfo)) {
            log.error("未进入考勤范围");

            return R.ok(false, "未进入考勤范围");
//            throw new RuntimeException("未进入考勤范围");
        }
        ProjectAttendance projectAttendance = this.getById(attendanceId);
        LocalTime time = LocalTime.now();
        // 上班打卡 考勤状态,
        // 5. 上班未打卡定义为旷工
        // 6. 若该次打卡在上班时间之前或下班时间之后，则定义为下班漏打卡 6；
        // 8. 若该次打卡在上班时间之后下班时间之前，则定义为 8 漏打卡迟到；
        if (projectAttendance.getCheckInTime() == null && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setCheckInTime(time);
            projectAttendance.setCheckInArea(attendancePointInfo.getPointAddress());
            if (time.isBefore(projectAttendance.getWorkTime())) {
                projectAttendance.setResult(AttendanceStatusEnum.MISSED_CLOCKING.code);
            } else {
                projectAttendance.setResult(AttendanceStatusEnum.LATE_AND_MISSED_CLOCKING.code);
            }
            return R.ok(this.updateById(projectAttendance));
        }
        // 无效打卡： 在上班之前打下班卡
        if (time.isBefore(projectAttendance.getWorkTime())) {
            log.error("无效打卡：在上班之前无法打下班卡");
            return R.ok(false, "无效打卡：在上班之前无法打下班卡");
//            throw new RuntimeException("无效打卡：在上班之前无法打下班卡");
        }
        // 下班打卡 考勤状态,
        // 1. 正常上班打卡，若该次打卡在任意时间，则定义为正常上班打卡 1
        // 2. 上班打卡迟到，若该次打卡在下班时间之后，则定义为迟到 2； 若该次打卡在下班时间之前，则定义为迟到 4
        // 3. 正常上班，若该次打卡在下班时间之前，则定义为早退 3；若该次打卡在下班时间之后，则定义为正常上班打卡 1；若下班卡早退，第三次打卡在下班时间之后，则定义为正常上班打卡 1
        // 4. 上下班卡迟到、早退 4，第三次打卡在下班时间之后，则定义为迟到 2
        // 5. 上班打卡旷工，若该次打卡在下班时间之后，则定义为漏打卡 7；若该次打卡在下班时间之前，则定义为 早退、漏打卡 9
        // 6. 下班漏打卡，若该次打卡在下班时间之后，则定义为正常上班打卡 1；若该次打卡在下班时间之前，则定义为早退 3
        // 7. 上班漏打卡，若该次打卡在下班时间之后，则定义为漏打卡 7；若该次打卡在下班时间之前，则定义为 早退、漏打卡 9
        // 8. 迟到漏打卡 , 若该次打卡在下班时间之后，则定义为上班打卡迟到 2；若该次打卡在下班时间之前，则定义为迟到、早退 4
        // 9. 早退、漏打卡 ,若该次打卡在下班时间之后，则定义为漏打卡 7；若该次打卡在下班时间之前，则定义为早退、漏打卡 9
        projectAttendance.setCheckOutTime(LocalTime.now());
        projectAttendance.setCheckOutArea(attendancePointInfo.getPointAddress());
        if (AttendanceStatusEnum.ABSENTEEISM.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.MORNING_MISSED_CLOCKING.code);
        }
        if (AttendanceStatusEnum.ABSENTEEISM.code.equals(projectAttendance.getResult()) && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LEAVEEARLY_AND_MISSED_CLOCKING.code);
        }
        if (AttendanceStatusEnum.MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LEAVEEARLY.code);
        }
        if (AttendanceStatusEnum.MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.NORMAL.code);
        }
        if (AttendanceStatusEnum.MORNING_MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LEAVEEARLY_AND_MISSED_CLOCKING.code);
        }
        if (AttendanceStatusEnum.LATE_AND_MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LATE.code);
        }
        if (AttendanceStatusEnum.LATE_AND_MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LATE_AND_LEAVEEARLY.code);
        }
        if (AttendanceStatusEnum.LEAVEEARLY_AND_MISSED_CLOCKING.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.MORNING_MISSED_CLOCKING.code);
        }
        if (AttendanceStatusEnum.LATE.code.equals(projectAttendance.getResult()) && time.isBefore(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LATE_AND_LEAVEEARLY.code);
        }
        if (AttendanceStatusEnum.LEAVEEARLY.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.NORMAL.code);
        }
        if (AttendanceStatusEnum.LATE_AND_LEAVEEARLY.code.equals(projectAttendance.getResult()) && time.isAfter(projectAttendance.getOffworkTime())) {
            projectAttendance.setResult(AttendanceStatusEnum.LATE.code);
        }

        return R.ok(this.updateById(projectAttendance));
    }

    @Override
    public ProjectCountAttendanceInfoVo countAttendanceInfo(String staffId, LocalDate date) {
        List<ProjectAttendanceInfoVo> attendanceInfoList = new ArrayList<>();
        ProjectCountAttendanceInfoVo countAttendanceInfoVo = new ProjectCountAttendanceInfoVo();
        List<ProjectAttendance> schedulingPlanList = projectStaffService.getSchedulingPlan(staffId, date);

        for (ProjectAttendance projectAttendance : schedulingPlanList) {
            ProjectAttendanceInfoVo projectAttendanceInfoVo = new ProjectAttendanceInfoVo();
            BeanUtil.copyProperties(projectAttendance, projectAttendanceInfoVo);
            attendanceInfoList.add(projectAttendanceInfoVo);
            countAttendanceInfoVo = computeAttendanceDay(countAttendanceInfoVo, projectAttendance.getResult());
        }
        countAttendanceInfoVo.setAttendanceInfoList(attendanceInfoList);
        return countAttendanceInfoVo;
    }

    @Override
    public Page<ProjectAttendanceCountPageVo> countDeptAttendancePage(Page page, Integer deptId, String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", fmt);
        Page<ProjectStaffShiftDetailPageVo> staffShiftDetailPage = projectStaffService.getDeptSchedulingPage(page, deptId, localDate.getYear(), localDate.getMonthValue());
        List<ProjectAttendanceCountPageVo> attendanceCountPageVoList = new ArrayList<>();
        Page<ProjectAttendanceCountPageVo> attendanceCountPageVoPage = new Page<>();
        BeanUtil.copyProperties(staffShiftDetailPage, attendanceCountPageVoPage);
        for (ProjectStaffShiftDetailPageVo record : staffShiftDetailPage.getRecords()) {
            ProjectAttendanceCountPageVo attendanceCountPageVo = new ProjectAttendanceCountPageVo();
            attendanceCountPageVo.setStaffId(record.getStaffId());
            attendanceCountPageVo.setStaffName(record.getStaffName());
            if (StrUtil.isNotBlank(record.getPlanId())) {
                attendanceCountPageVo.setStatus("1");
                List<ProjectAttendance> schedulingPlanList = projectStaffService.getSchedulingPlan(record.getStaffId(), localDate);
                for (ProjectAttendance projectAttendance : schedulingPlanList) {
                    if (StrUtil.isBlank(projectAttendance.getResult())) {
                        continue;
                    }
                    // 不是休息应勤天数加一
                    if (!AttendanceStatusEnum.REST.code.equals(projectAttendance.getResult())) {
                        attendanceCountPageVo.setBeDiligentDay(attendanceCountPageVo.getBeDiligentDay() + 1);
                    }
                    switch (projectAttendance.getResult()) {
                        case "0":
                            break;
                        case "1":
                            attendanceCountPageVo.setNormalDay(attendanceCountPageVo.getNormalDay() + 1);
                            break;
                        case "2":
                            attendanceCountPageVo.setLateDay(attendanceCountPageVo.getLateDay() + 1);
                            break;
                        case "3":
                            attendanceCountPageVo.setLeaveEarlyDay(attendanceCountPageVo.getLeaveEarlyDay() + 1);
                            break;
                        case "4":
                            attendanceCountPageVo.setLateDay(attendanceCountPageVo.getLateDay() + 1);
                            attendanceCountPageVo.setLeaveEarlyDay(attendanceCountPageVo.getLeaveEarlyDay() + 1);
                            break;
                        case "5":
                            attendanceCountPageVo.setAbsenteeismDay(attendanceCountPageVo.getAbsenteeismDay() + 1);
                            break;
                        case "6":
                            attendanceCountPageVo.setMissedClockingDay(attendanceCountPageVo.getMissedClockingDay() + 1);
                            break;
                        case "7":
                            attendanceCountPageVo.setMissedClockingDay(attendanceCountPageVo.getMissedClockingDay() + 1);
                            break;
                        case "8":
                            attendanceCountPageVo.setLateDay(attendanceCountPageVo.getLateDay() + 1);
                            attendanceCountPageVo.setMissedClockingDay(attendanceCountPageVo.getMissedClockingDay() + 1);
                            break;
                        case "9":
                            attendanceCountPageVo.setLeaveEarlyDay(attendanceCountPageVo.getLeaveEarlyDay() + 1);
                            attendanceCountPageVo.setMissedClockingDay(attendanceCountPageVo.getMissedClockingDay() + 1);
                            break;

                    }
                }
            }
            attendanceCountPageVoList.add(attendanceCountPageVo);
        }
        attendanceCountPageVoPage.setRecords(attendanceCountPageVoList);
        return attendanceCountPageVoPage;
    }

    @Override
    public ProjectDeptCountAttendanceVo countDeptAttendance(Integer deptId, String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", fmt);
        List<ProjectStaffShiftDetailPageVo> staffShiftDetailPage = projectStaffService.getDeptScheduling(deptId);
        ProjectDeptCountAttendanceVo deptCountAttendanceVo = new ProjectDeptCountAttendanceVo();

        for (ProjectStaffShiftDetailPageVo record : staffShiftDetailPage) {
            if (StrUtil.isNotBlank(record.getPlanId())) {

                List<ProjectAttendance> schedulingPlanList = projectStaffService.getSchedulingPlan(record.getStaffId(), localDate);

                for (ProjectAttendance projectAttendance : schedulingPlanList) {
                    if (StrUtil.isBlank(projectAttendance.getResult())) {
                        continue;
                    }
                    switch (projectAttendance.getResult()) {
                        case "0":
                            break;
                        case "1":
                            break;
                        case "2":
                            deptCountAttendanceVo.setLateDay(deptCountAttendanceVo.getLateDay() + 1);
                            break;
                        case "3":
                            deptCountAttendanceVo.setLeaveEarlyDay(deptCountAttendanceVo.getLeaveEarlyDay() + 1);
                            break;
                        case "4":
                            deptCountAttendanceVo.setLateDay(deptCountAttendanceVo.getLateDay() + 1);
                            deptCountAttendanceVo.setLeaveEarlyDay(deptCountAttendanceVo.getLeaveEarlyDay() + 1);
                            break;
                        case "5":
                            deptCountAttendanceVo.setAbsenteeismDay(deptCountAttendanceVo.getAbsenteeismDay() + 1);
                            break;
                        case "6":
                            deptCountAttendanceVo.setMissedClockingDay(deptCountAttendanceVo.getMissedClockingDay() + 1);
                            break;
                        case "7":
                            deptCountAttendanceVo.setMissedClockingDay(deptCountAttendanceVo.getMissedClockingDay() + 1);
                            break;
                        case "8":
                            deptCountAttendanceVo.setLateDay(deptCountAttendanceVo.getLateDay() + 1);
                            deptCountAttendanceVo.setMissedClockingDay(deptCountAttendanceVo.getMissedClockingDay() + 1);
                            break;
                        case "9":
                            deptCountAttendanceVo.setLeaveEarlyDay(deptCountAttendanceVo.getLeaveEarlyDay() + 1);
                            deptCountAttendanceVo.setMissedClockingDay(deptCountAttendanceVo.getMissedClockingDay() + 1);
                            break;
                    }
                }
            }
        }
        return deptCountAttendanceVo;
    }

    private ProjectCountAttendanceInfoVo computeAttendanceDay(ProjectCountAttendanceInfoVo countAttendanceInfoVo, String result) {
        if (StrUtil.isBlank(result)) {
            return countAttendanceInfoVo;
        }
        // 不是休息应勤天数加一
        if (!AttendanceStatusEnum.REST.code.equals(result)) {
            countAttendanceInfoVo.setBeDiligentDay(countAttendanceInfoVo.getBeDiligentDay() + 1);
        }
        switch (result) {
            case "0": // 休息
                break;
            case "1": // 正常上班
                countAttendanceInfoVo.setNormalDay(countAttendanceInfoVo.getNormalDay() + 1);
                break;
            case "2":
                countAttendanceInfoVo.setLateDay(countAttendanceInfoVo.getLateDay() + 1);
                break;
            case "3":
                countAttendanceInfoVo.setLeaveEarlyDay(countAttendanceInfoVo.getLeaveEarlyDay() + 1);
                break;
            case "4":
                countAttendanceInfoVo.setLateDay(countAttendanceInfoVo.getLateDay() + 1);
                countAttendanceInfoVo.setLeaveEarlyDay(countAttendanceInfoVo.getLeaveEarlyDay() + 1);
                break;
            case "5":
                countAttendanceInfoVo.setAbsenteeismDay(countAttendanceInfoVo.getAbsenteeismDay() + 1);
                break;
            case "6": // 漏打卡
                countAttendanceInfoVo.setMissedClockingDay(countAttendanceInfoVo.getMissedClockingDay() + 1);
                break;
            case "7": // 漏打卡
                countAttendanceInfoVo.setMissedClockingDay(countAttendanceInfoVo.getMissedClockingDay() + 1);
                break;
            case "8":
                countAttendanceInfoVo.setLateDay(countAttendanceInfoVo.getLateDay() + 1);
                countAttendanceInfoVo.setMissedClockingDay(countAttendanceInfoVo.getMissedClockingDay() + 1);
                break;
            case "9":
                countAttendanceInfoVo.setLeaveEarlyDay(countAttendanceInfoVo.getLeaveEarlyDay() + 1);
                countAttendanceInfoVo.setMissedClockingDay(countAttendanceInfoVo.getMissedClockingDay() + 1);
                break;
        }
        return countAttendanceInfoVo;
    }
}
