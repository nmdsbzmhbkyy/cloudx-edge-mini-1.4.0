package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.PositionEnum;
import com.aurine.cloudx.estate.service.ProjectAttendanceService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.GpsUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("attendance")
@Api(value = "attendance", tags = "考勤打卡管理")
public class ProjectAttendanceController {
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectAttendanceService projectAttendanceService;

    @GetMapping
    @ApiOperation(value = "获取打卡信息", notes = "获取打卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = false, paramType = "param"),
            @ApiImplicitParam(name = "lon", value = "经度", required = false, paramType = "param"),
    })
    public R<ProjectAttendanceVo> getAttendanceInfo (@RequestParam(value = "lat", required = false) Double lat, @RequestParam(value = "lon", required = false) Double lon) {
        GPS gps = GpsUtil.gcj02_To_Bd09(lat, lon);
        return R.ok(projectAttendanceService.getAttendanceInfo(gps.getLat(), gps.getLon()));
    }

    @PutMapping
    @ApiOperation(value = "上下班打卡", notes = "上下班打卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
    })
    public R<Boolean> workAttendance (@RequestBody AppAttendanceLocalFormVo attendanceLocalFormVo) {
        GPS gps = GpsUtil.gcj02_To_Bd09(attendanceLocalFormVo.getLat(), attendanceLocalFormVo.getLon());
        return projectAttendanceService.clockIn(attendanceLocalFormVo.getAttendanceId(), gps.getLat(), gps.getLon());
    }

    @GetMapping("/count")
    @ApiOperation(value = "统计当前用户考勤信息", notes = "统计考勤信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期 （格式为 yyyy-MM）", required = true, paramType = "param")

    })
    public R<ProjectCountAttendanceInfoVo> getAttendanceInfo (@RequestParam("date") String date) {
        ProjectStaffVo staffVo = projectStaffService.getStaffByOwner();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", fmt);
        ProjectCountAttendanceInfoVo countAttendanceInfoVo = projectAttendanceService.countAttendanceInfo(staffVo.getStaffId(), localDate);
        return R.ok(countAttendanceInfoVo);
    }

    @GetMapping("/count/{staffId}")
    @ApiOperation(value = "根据员工ID统计考勤信息", notes = "统计考勤信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "date", value = "日期 （格式为 yyyy-MM）", required = true, paramType = "param")

    })
    public R<ProjectCountAttendanceInfoVo> getByStaffId (@PathVariable String staffId, @RequestParam("date") String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date + "-01", fmt);
        ProjectCountAttendanceInfoVo countAttendanceInfoVo = projectAttendanceService.countAttendanceInfo(staffId, localDate);
        return R.ok(countAttendanceInfoVo);
    }


    @GetMapping("/dept-count/page")
    @ApiOperation(value = "部门考勤列表", notes = "部门考勤列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期 （格式为 yyyy-MM）", required = true, paramType = "param"),
            @ApiImplicitParam(name = "deptId", value = "部门ID", required = false, paramType = "param")

    })
    public R<Page<ProjectAttendanceCountPageVo>> page (Page page, @RequestParam("date") String date, @RequestParam("deptId") Integer deptId) {
        ProjectStaffVo staffVo = projectStaffService.getStaffByOwner();
        if (PositionEnum.STAFF.code.equals(staffVo.getGrade())) {
            return R.failed("员工不能查看");
        }
        // 部门负责人
        if (PositionEnum.MANAGER.code.equals(staffVo.getGrade())) {
            return R.ok(projectAttendanceService.countDeptAttendancePage(page, staffVo.getDepartmentId(), date));
        }
        // 项目负责人
        if (PositionEnum.PERSON_IN_CHANGE.code.equals(staffVo.getGrade())) {
            return R.ok(projectAttendanceService.countDeptAttendancePage(page, deptId, date));
        }
        return R.ok(page);
    }

    @GetMapping("/dept-count")
    @ApiOperation(value = "部门考勤汇总", notes = "部门考勤汇总")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期 （格式为 yyyy-MM）", required = true, paramType = "param"),
            @ApiImplicitParam(name = "deptId", value = "部门ID", required = false, paramType = "param")
    })
    public R<ProjectDeptCountAttendanceVo> sumCount (@RequestParam("date") String date, @RequestParam("deptId") Integer deptId) {
        ProjectStaffVo staffVo = projectStaffService.getStaffByOwner();
        if (PositionEnum.STAFF.code.equals(staffVo.getGrade())) {
            return R.failed("员工不能查看");
        }
        ProjectDeptCountAttendanceVo deptCountAttendanceVo = new ProjectDeptCountAttendanceVo();
        // 部门负责人
        if (PositionEnum.MANAGER.code.equals(staffVo.getGrade())) {
            deptCountAttendanceVo = projectAttendanceService.countDeptAttendance(staffVo.getDepartmentId(), date);
            deptCountAttendanceVo.setDeptName(staffVo.getDeptName());
        }
        // 项目负责人
        if (PositionEnum.PERSON_IN_CHANGE.code.equals(staffVo.getGrade())) {
            deptCountAttendanceVo = projectAttendanceService.countDeptAttendance(deptId, date);
        }
        deptCountAttendanceVo.setDate(date);
        return R.ok(deptCountAttendanceVo);
    }
}

