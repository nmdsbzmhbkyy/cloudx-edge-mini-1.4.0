package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.WorkAttendanceEnum;
import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.entity.ProjectShiftConf;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.estate.excel.ProjectAttendanceExcel;
import com.aurine.cloudx.estate.excel.ProjectAttendanceSummaryExcel;
import com.aurine.cloudx.estate.excel.projectAttendance.CellColorSheetWriteHandler;
import com.aurine.cloudx.estate.mapper.ProjectAttendanceMapper;
import com.aurine.cloudx.estate.service.ProjectAttendanceService;
import com.aurine.cloudx.estate.service.ProjectShiftConfService;
import com.aurine.cloudx.estate.service.ProjectStaffShiftDetailService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import com.aurine.cloudx.estate.util.HolidayUtil;
import com.aurine.cloudx.estate.vo.ProjectAttdanceSummaryVo;
import com.aurine.cloudx.estate.vo.ProjectAttendanceQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 考勤打卡(ProjectAttendancePoint)表服务实现类
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
@Service
public class ProjectAttendanceServiceImpl extends ServiceImpl<ProjectAttendanceMapper, ProjectAttendance> implements ProjectAttendanceService {
    @Resource
    private ProjectAttendanceMapper projectAttendanceMapper;
    @Resource
    private ProjectStaffShiftDetailService projectStaffShiftDetailService;
    @Resource
    private ProjectShiftConfService projectShiftConfService;


    ProjectStaffShiftDetail getStaffShiftDetail(String staffId, Integer year, Integer month) {
        Integer day = 31;
        JSONObject projectStaffShiftDetailObj = JSONUtil.createObj();

        List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        for (ProjectStaffShiftDetail staffShiftDetail : staffShiftDetailList) {
            JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
            for (Integer i = 1; i <= day; i++) {
                String shiftName = (String) obj.get("day" + i);
                if (shiftName != null && !"".equals(shiftName) && !"休息".equals(shiftName) && !"排休".equals(shiftName)) {
                    projectStaffShiftDetailObj.set("day" + i, shiftName);
                }
            }
        }

        ProjectStaffShiftDetail projectStaffShiftDetail = JSONUtil.toBean(projectStaffShiftDetailObj, ProjectStaffShiftDetail.class);
        projectStaffShiftDetail.setStaffId(staffId);
        projectStaffShiftDetail.setPlanYear(String.valueOf(year));
        projectStaffShiftDetail.setPlanMonth(String.valueOf(month));
        return projectStaffShiftDetail;
    }

    @Override
    public IPage<ProjectAttendanceQueryVo> page(Page page, ProjectAttendanceQueryVo query) {
        query.setProjectId(ProjectContextHolder.getProjectId());
        query.setTenantId(TenantContextHolder.getTenantId());
        String[] dateRange = query.getDateRange();
        if (null != dateRange && dateRange.length == 2) {
            LocalDate yearMonth1 = LocalDate.parse(dateRange[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate yearMonth2 = LocalDate.parse(dateRange[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            query.setYear(String.valueOf(yearMonth1.getYear()));
            query.setMonth(String.valueOf(yearMonth1.getMonthValue()));
            query.setYear2(String.valueOf(yearMonth2.getYear()));
            query.setMonth2(String.valueOf(yearMonth2.getMonthValue()));
        }
        IPage<ProjectAttendanceQueryVo> projectAttendancePage = projectAttendanceMapper.page(page, query);
        projectAttendancePage.getRecords().stream().map(e -> {
            LocalDate date = e.getAttenDate();
            Integer year = date.getYear();
            Integer month = date.getMonthValue();
            Integer day = date.getMonth().maxLength();
            if (StringUtil.isNotEmpty(e.getDetailId())) {
                //有排班计划
                ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(e.getStaffId(), year, month);
                JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
                for (int i = 1; i <= day; i++) {
                    String shiftName = (String) obj.get("day" + i);
                    boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);
                    LocalDate newDate = LocalDate.of(year, month, i);
                    if (newDate.isEqual(e.getAttenDate()) && e.getResult().equals(WorkAttendanceEnum.ABSENT.code)) {
                        if (isRest) {
                            e.setResult("0");
                        }

                    }
                }
            }
            return e;
        }).collect(Collectors.toList());
        projectAttendancePage.setRecords(projectAttendancePage.getRecords());
        return projectAttendancePage;
    }

    @Override
    public List<ProjectAttendanceQueryVo> getProjectAttendanceList(ProjectAttendanceQueryVo query) {


        query.setProjectId(ProjectContextHolder.getProjectId());
        query.setTenantId(TenantContextHolder.getTenantId());
        String[] dateRange = query.getDateRange();
        if (null != dateRange && dateRange.length == 2) {
            LocalDate yearMonth1 = LocalDate.parse(dateRange[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate yearMonth2 = LocalDate.parse(dateRange[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            query.setYear(String.valueOf(yearMonth1.getYear()));
            query.setMonth(String.valueOf(yearMonth1.getMonthValue()));
            query.setYear2(String.valueOf(yearMonth2.getYear()));
            query.setMonth2(String.valueOf(yearMonth2.getMonthValue()));
        }
        List<ProjectAttendanceQueryVo> list = projectAttendanceMapper.list(query);
        list.stream().map(e -> {
            System.out.println(StringUtil.isNotEmpty(e.getDetailId()));
            LocalDate date = e.getAttenDate();
            Integer year = date.getYear();
            Integer month = date.getMonthValue();
            Integer day = date.getMonth().maxLength();
            if (StringUtil.isNotEmpty(e.getDetailId())) {
                //有排班计划
                ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(e.getStaffId(), year, month);
                JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
                int num = 0;
                for (int i = 1; i <= day; i++) {
                    String shiftName = (String) obj.get("day" + i);
                    boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);
                    LocalDate newDate = LocalDate.of(year, month, i);
                    if (newDate.isEqual(e.getAttenDate()) && e.getResult().equals(WorkAttendanceEnum.ABSENT.code)) {
                        num++;
                        if (isRest) {
                            e.setResult(WorkAttendanceEnum.REST.code);
                        }

                    }
                }
            }
            return e;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public void exportExcel(HttpServletResponse httpServletResponse, ProjectAttendanceQueryVo ProjectAttendanceQueryVo) {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //根据条件查询考勤记录
        List<ProjectAttendanceQueryVo> records = this.getProjectAttendanceList(ProjectAttendanceQueryVo);
        if (CollectionUtil.isNotEmpty(ProjectAttendanceQueryVo.getSelectRow())) {
            records = ProjectAttendanceQueryVo.getSelectRow();
        }
        List<ProjectAttendanceExcel> record2 = new ArrayList<>();
        records.stream().forEach(r -> {
            ProjectAttendanceExcel projectAttendanceExcel = new ProjectAttendanceExcel();
            BeanUtils.copyProperties(r, projectAttendanceExcel);
            record2.add(projectAttendanceExcel);
        });
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        String sheetName = "考勤情况";
        if (null != ProjectAttendanceQueryVo.getDateRange() && ProjectAttendanceQueryVo.getDateRange().length > 0) {
            sheetName = String.format("%s至%s 考勤情况", ProjectAttendanceQueryVo.getDateRange()[0], ProjectAttendanceQueryVo.getDateRange()[1]);
        }
        CellColorSheetWriteHandler writeHandler = new CellColorSheetWriteHandler(record2, IndexedColors.RED.getIndex());

        EasyExcel.write(getOutputStream("考勤情况", httpServletResponse), ProjectAttendanceExcel.class).registerWriteHandler(horizontalCellStyleStrategy).registerWriteHandler(writeHandler).sheet(sheetName).doWrite(record2);
    }

    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            return response.getOutputStream();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IPage<ProjectAttdanceSummaryVo> attendanceSummaryPage(Page page, ProjectAttdanceSummaryVo query) {
        query.setProjectId(ProjectContextHolder.getProjectId());
        query.setTenantId(TenantContextHolder.getTenantId());
        //获取本月第一天与最后一天
        LocalDate firstDay = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.firstDayOfMonth());
        // 如果查询的是这个月的 需要取截止到今天的时间
        Integer queryMonth = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).getMonthValue();
        Integer nowMonth = LocalDate.now().getMonthValue();
        LocalDate lastDay = null;
        if (queryMonth.equals(nowMonth)) {
            lastDay = LocalDate.now();
        } else {
            lastDay = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.lastDayOfMonth());
        }
        LocalDate[] dateRange = new LocalDate[2];
        dateRange[0] = firstDay;
        dateRange[1] = lastDay;
        query.setDateRange(dateRange);
        IPage<ProjectAttdanceSummaryVo> projectAttdanceSummaryVoIPage = projectAttendanceMapper.attendanceSummaryPage(page, query);
        List<ProjectAttdanceSummaryVo> records = projectAttdanceSummaryVoIPage.getRecords();
        records = records.stream().map(e -> {
            int num = 0; //初始化旷工天数

            e.setAttenDate(LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            LocalDate date = e.getAttenDate();
            Integer year = date.getYear();
            Integer month = date.getMonthValue();
            Integer day = date.getMonth().maxLength();

            List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                    .eq(ProjectStaffShiftDetail::getStaffId, e.getStaffId())
                    .eq(ProjectStaffShiftDetail::getPlanYear, year)
                    .eq(ProjectStaffShiftDetail::getPlanMonth, month));
            if (CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                //有排班计划
                ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(e.getStaffId(), year, month);
                JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
                long days = LocalDate.now().toEpochDay() - date.toEpochDay();
                for (int i = 1; i <= days; i++) {
                    String shiftName = (String) obj.get("day" + i);
                    //是否为休息
                    boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);

                    //当天不是休息 并且当天没有打卡记录 或者当天的打卡记录是旷工
                    if (!isRest) {
                        LocalDate newDate = LocalDate.of(year, month, i);
                        ProjectAttendance projectAttendance = this.getOne(new QueryWrapper<ProjectAttendance>().lambda()
                                .eq(ProjectAttendance::getAttenDate, newDate)
                                .eq(ProjectAttendance::getStaffId, e.getStaffId()));
                        if (null == projectAttendance) {
                            num++;
                        } else if (projectAttendance.getResult().equals(WorkAttendanceEnum.ABSENT.code)) {
                            num++;
                        }
                    }
                }
                e.setWorkDay(null == projectStaffShiftDetailService.getAttendanceDay(e.getStaffId(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) ? 0
                        : projectStaffShiftDetailService.getAttendanceDay(e.getStaffId(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                e.setOutWork(num);
            } else {
                // 打卡list
//                long days;
//                if (queryMonth.equals(nowMonth)) {
//                     days = LocalDate.now().toEpochDay() - date.toEpochDay();
//                } else {
//                     days = e.getWorkDay();
//                }
//                List<ProjectAttendance> attendanceList = this.list(new QueryWrapper<ProjectAttendance>().lambda()
//                        .eq(ProjectAttendance::getStaffId, e.getStaffId())
//                        .between(ProjectAttendance::getAttenDate, query.getYearMonth(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.lastDayOfMonth()))
//                        .ne(ProjectAttendance::getResult, WorkAttendanceEnum.ABSENT.code));

                // 旷工=应勤-有打卡的记录+旷工打卡
                e.setOutWork(0);
                e.setWorkDay(0);
            }


            return e;
        }).collect(Collectors.toList());
        projectAttdanceSummaryVoIPage.setRecords(records);
        return projectAttdanceSummaryVoIPage;
    }

    @Override
    public List<ProjectAttdanceSummaryVo> listCount(ProjectAttdanceSummaryVo query) {
        query.setProjectId(ProjectContextHolder.getProjectId());
        query.setTenantId(TenantContextHolder.getTenantId());
        //获取本月第一天与最后一天
        LocalDate firstDay = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.firstDayOfMonth());
        // 如果查询的是这个月的 需要取截止到今天的时间
        Integer queryMonth = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).getMonthValue();
        Integer nowMonth = LocalDate.now().getMonthValue();
        LocalDate lastDay = null;
        if (queryMonth.equals(nowMonth)) {
            lastDay = LocalDate.now();
        } else {
            lastDay = LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.lastDayOfMonth());
        }
        LocalDate[] dateRange = new LocalDate[2];
        dateRange[0] = firstDay;
        dateRange[1] = lastDay;
        query.setDateRange(dateRange);

        List<ProjectAttdanceSummaryVo> projectAttdanceSummaryVos = projectAttendanceMapper.attendanceSummaryList(query);
        projectAttdanceSummaryVos = projectAttdanceSummaryVos.stream().map(e -> {
            int num = 0; //初始化旷工天数


            e.setAttenDate(LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            LocalDate date = e.getAttenDate();
            Integer year = date.getYear();
            Integer month = date.getMonthValue();
            Integer day = date.getMonth().maxLength();
            // 根据员工查询排班记录
            List<ProjectStaffShiftDetail> staffShiftDetailList = projectStaffShiftDetailService.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                    .eq(ProjectStaffShiftDetail::getStaffId, e.getStaffId())
                    .eq(ProjectStaffShiftDetail::getPlanYear, year)
                    .eq(ProjectStaffShiftDetail::getPlanMonth, month));
            if (CollectionUtil.isNotEmpty(staffShiftDetailList)) {
                //有排班计划
                ProjectStaffShiftDetail staffShiftDetail = getStaffShiftDetail(e.getStaffId(), year, month);
                JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
                long days = LocalDate.now().toEpochDay() - date.toEpochDay();
                for (int i = 1; i <= days; i++) {
                    String shiftName = (String) obj.get("day" + i);
                    //是否为休息
                    boolean isRest = shiftName == null || "".equals(shiftName) || "休息".equals(shiftName) || "排休".equals(shiftName);

                    //当天不是休息 并且当天没有打卡记录 或者当天的打卡记录是旷工
                    if (!isRest) {
                        LocalDate newDate = LocalDate.of(year, month, i);
                        ProjectAttendance projectAttendance = this.getOne(new QueryWrapper<ProjectAttendance>().lambda()
                                .eq(ProjectAttendance::getAttenDate, newDate)
                                .eq(ProjectAttendance::getStaffId, e.getStaffId()));
                        if (null == projectAttendance) {
                            num++;
                        } else if (projectAttendance.getResult().equals(WorkAttendanceEnum.ABSENT.code)) {
                            num++;
                        }
                    }
                }
                e.setOutWork(num);
                e.setWorkDay(null == projectStaffShiftDetailService.getAttendanceDay(e.getStaffId(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) ? 0
                        : projectStaffShiftDetailService.getAttendanceDay(e.getStaffId(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            } else {
                // 打卡list
//                long days;
//                // 如果查询是当月的 就取1号到昨天
//                if (queryMonth.equals(nowMonth)) {
//                    days = LocalDate.now().toEpochDay() - date.toEpochDay();
//                } else {
//                    //不是单月的取应勤天数
//                    days = e.getWorkDay();
//                }
//
//                List<ProjectAttendance> attendanceList = this.list(new QueryWrapper<ProjectAttendance>().lambda()
//                        .eq(ProjectAttendance::getStaffId, e.getStaffId())
//                        .between(ProjectAttendance::getAttenDate, query.getYearMonth(), LocalDate.parse(query.getYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).with(TemporalAdjusters.lastDayOfMonth()))
//                        .ne(ProjectAttendance::getResult, "5"));

                // 旷工=应勤-有打卡的记录+旷工打卡
                e.setOutWork(0);
                e.setWorkDay(0);
            }


            return e;
        }).collect(Collectors.toList());
        return projectAttdanceSummaryVos;
    }

    @Override
    public void exportAttendanceSummaryExcel(HttpServletResponse httpServletResponse, ProjectAttdanceSummaryVo projectAttdanceSummaryVo) {
        List<ProjectAttdanceSummaryVo> projectAttdanceSummaryVos = new ArrayList<>();
        List<ProjectAttendanceSummaryExcel> record2 = new ArrayList<>();
        //selectRow 手动选择哪些行 如果不是手动选择则导出所有数据
        if (CollectionUtil.isNotEmpty(projectAttdanceSummaryVo.getSelectRow())) {
            projectAttdanceSummaryVos = projectAttdanceSummaryVo.getSelectRow();
        }else {
            projectAttdanceSummaryVos = this.listCount(projectAttdanceSummaryVo);
        }
        projectAttdanceSummaryVos.stream().forEach(r -> {
            ProjectAttendanceSummaryExcel projectAttendanceSummaryExcel = new ProjectAttendanceSummaryExcel();
            BeanUtils.copyProperties(r, projectAttendanceSummaryExcel);
            record2.add(projectAttendanceSummaryExcel);
        });
        String yearMonth = projectAttdanceSummaryVo.getYearMonth().substring(0, projectAttdanceSummaryVo.getYearMonth().lastIndexOf("-"));
        String sheetName = yearMonth + "考勤情况汇总";
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(getOutputStream(yearMonth + "考勤情况汇总", httpServletResponse), ProjectAttendanceSummaryExcel.class).registerWriteHandler(horizontalCellStyleStrategy).sheet(sheetName).doWrite(record2);
    }
}
