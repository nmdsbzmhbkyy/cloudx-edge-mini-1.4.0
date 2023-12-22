package com.aurine.cloudx.estate.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author cjw
 * @description: 考勤汇总导出execl
 * @date 2021/3/11 11:19
 */
@Data
public class ProjectAttendanceSummaryExcel {
    @ExcelProperty(value = "员工姓名", index = 0)
    @ColumnWidth(value = 20)
    private String staffName;
    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称", index = 1)
    @ColumnWidth(value = 20)
    private String deptName;

    /**
     * 应勤天数
     */
    @ExcelProperty(value = "应勤天数", index = 2)
    @ColumnWidth(value = 20)
    private Integer workDay;
    /**
     * 正常上班天数
     */
    @ExcelProperty(value = "正常(天)", index = 3)
    @ColumnWidth(value = 20)
    private Integer normalWorkDay;

    /**
     * 迟到天数
     */
    @ExcelProperty(value = "迟到(次)", index = 4)
    @ColumnWidth(value = 20)
    private Integer lateDay;
    /**
     * 早退天数
     */
    @ExcelProperty(value = "早退(次)", index = 5)
    @ColumnWidth(value = 20)
    private Integer leaveEarlyDay;
    /**
     * 旷工天数
     */
    @ExcelProperty(value = "旷工(次)", index = 6)
    @ColumnWidth(value = 20)
    private Integer outWork;
}
