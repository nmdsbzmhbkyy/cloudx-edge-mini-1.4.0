package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectCountAttendanceInfoVo {
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
     * 打卡信息列表
     */
    @ApiModelProperty(value = "打卡信息列表")
    private List<ProjectAttendanceInfoVo> attendanceInfoList;
}
