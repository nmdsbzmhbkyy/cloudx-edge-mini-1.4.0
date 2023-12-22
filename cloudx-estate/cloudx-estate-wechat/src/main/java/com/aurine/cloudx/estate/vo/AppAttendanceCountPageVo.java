package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门考勤分页视图")
public class AppAttendanceCountPageVo {

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
     * 应勤天数
     */
    @ApiModelProperty(value = "应勤天数")
    private Integer beDiligentDay;
    /**
     * 正常上班天数
     */
    @ApiModelProperty(value = "正常上班天数")
    private Integer normalDay;


    /**
     * 旷工天数
     */
    @ApiModelProperty(value = "旷工天数")
    private Integer absenteeismDay;
    /**
     * 迟到天数
     */
    @ApiModelProperty(value = "迟到天数")
    private Integer lateDay;
    /**
     * 早退天数
     */
    @ApiModelProperty(value = "早退天数")
    private Integer leaveEarlyDay;

}
