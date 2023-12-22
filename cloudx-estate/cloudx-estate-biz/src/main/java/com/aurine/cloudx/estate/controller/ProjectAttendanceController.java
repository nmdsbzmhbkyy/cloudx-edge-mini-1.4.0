package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.excel.ProjectAttendanceExcel;
import com.aurine.cloudx.estate.service.ProjectAttendanceService;
import com.aurine.cloudx.estate.util.HolidayUtil;
import com.aurine.cloudx.estate.vo.ProjectAttdanceSummaryVo;
import com.aurine.cloudx.estate.vo.ProjectAttendanceQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/projectAttendance")
@Api(value = "projectAttendance", tags = "考勤情况处理")
@Slf4j
public class ProjectAttendanceController {

    private final ProjectAttendanceService projectAttendanceService;


    /**
     * @description: 分页查询
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/3/5 11:05
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectAttendancePage(Page page, ProjectAttendanceQueryVo projectAttendanceQueryVo) {
        return R.ok(projectAttendanceService.page(page, projectAttendanceQueryVo));
    }

    /**
     * @description: 导出excel
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/3/5 15:59
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @Inner(false)
    public void download(@RequestBody ProjectAttendanceQueryVo projectAttendanceQueryVo, HttpServletResponse httpServletResponse) {
        projectAttendanceService.exportExcel(httpServletResponse, projectAttendanceQueryVo);
    }

    /**
     * @description: 考勤汇总分页查询
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/3/5 11:05
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/attendanceSummaryPage")
    public R attendanceSummary(Page page, ProjectAttdanceSummaryVo projectAttendanceQueryVo) {
        return R.ok(projectAttendanceService.attendanceSummaryPage(page,projectAttendanceQueryVo));
    }

    /**
     * @description:  导出考勤汇总excel
     * @param:
     * @return:
     * @author cjw
     * @date: 2021/3/11 11:56
     */
    @PostMapping("/exportAttendanceSummaryExcel")
    @ApiOperation(value = "导出考勤汇总excel", notes = "导出考勤汇总excel")
    @Inner(false)
    public void exportAttendanceSummaryExcel(@RequestBody ProjectAttdanceSummaryVo projectAttdanceSummaryVo, HttpServletResponse httpServletResponse) {
        projectAttendanceService.exportAttendanceSummaryExcel(httpServletResponse, projectAttdanceSummaryVo);
    }

}
