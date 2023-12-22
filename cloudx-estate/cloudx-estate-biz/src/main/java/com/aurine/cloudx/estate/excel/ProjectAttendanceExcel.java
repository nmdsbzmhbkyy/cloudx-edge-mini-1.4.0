package com.aurine.cloudx.estate.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.aurine.cloudx.estate.excel.converter.LocalDateConverter;
import com.aurine.cloudx.estate.excel.converter.localTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考勤记录表
 */
@Data
public class ProjectAttendanceExcel {
    @ExcelProperty(value = "姓名", index = 0)
    @ColumnWidth(value = 20)
    private String staffName;
    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门", index = 1)
    @ColumnWidth(value = 20)
    private String deptName;
    /**
     * 日期
     */
    @ExcelProperty(value = "日期", index = 2, converter = LocalDateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd ")
    @ColumnWidth(value = 20)
    private LocalDate attenDate;
    /**
     * 上班时间
     */
    @ExcelProperty(value = "上班时间", index = 3,converter = localTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalTime workTime;
    /**
     * 上班打卡时间
     */
    @ExcelProperty(value = "打卡时间", index = 4,converter = localTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalTime checkInTime;
    /**
     * 下班时间
     */
    @ExcelProperty(value = "下班时间", index = 5,converter = localTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalTime offworkTime;
    /**
     * 下班打卡时间
     */
    @ExcelProperty(value = "打卡时间", index = 6,converter = localTimeConverter.class)
    @ColumnWidth(value = 20)
    private LocalTime checkOutTime;
    /**
     * 考勤结果
     */
    @ExcelProperty(value = "考勤结果", index = 7)
    @ColumnWidth(value = 20)
    private String result;


}