package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门考勤汇总视图")
public class ProjectDeptCountAttendanceVo {

    /**
     * 旷工天数
     */
    @ApiModelProperty(value = "旷工天数")
    private Integer absenteeismDay = 0;
    /**
     * 迟到天数
     */
    @ApiModelProperty(value = "迟到天数")
    private Integer lateDay = 0;
    /**
     * 早退天数
     */
    @ApiModelProperty(value = "早退天数")
    private Integer leaveEarlyDay = 0;

    /**
     * 漏打卡
     */
    @ApiModelProperty(value = "漏打卡天数")
    private Integer missedClockingDay = 0;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "日期")
    private String date;
}
