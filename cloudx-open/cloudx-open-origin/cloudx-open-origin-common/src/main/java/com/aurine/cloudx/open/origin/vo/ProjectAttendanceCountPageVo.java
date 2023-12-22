package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门考勤分页视图")
public class ProjectAttendanceCountPageVo {

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String staffId;
    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String staffName;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;


    /**
     * 应勤天数
     */
    @ApiModelProperty(value = "应勤天数")
    private Integer beDiligentDay = 0;
    /**
     * 正常上班天数
     */
    @ApiModelProperty(value = "正常上班天数")
    private Integer normalDay = 0;


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
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 0. 未配置排班 1. 已配置排班")
    private String status = "0";

}
