package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ApiModel("考勤信息字段缩减表")
public class ProjectAttendanceInfoVo {
    /**
     * 考勤记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "考勤记录id")
    private String attendanceId;


    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;


    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String staffName;


    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer deptId;


    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;


    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private LocalDate attenDate;


    /**
     * 上班时间
     */
    @ApiModelProperty(value = "上班时间")
    private LocalTime workTime;


    /**
     * 上班打卡时间
     */
    @ApiModelProperty(value = "上班打卡时间")
    private LocalTime checkInTime;

    /**
     * 下班时间
     */
    @ApiModelProperty(value = "下班时间")
    private LocalTime offworkTime;


    /**
     * 下班打卡时间
     */
    @ApiModelProperty(value = "下班打卡时间")
    private LocalTime checkOutTime;

    /**
     * 上班打卡地点
     */
    @ApiModelProperty(value = "上班打卡地点")
    private String checkInArea;
    /**
     * 下班打卡地点
     */
    @ApiModelProperty(value = "下班打卡地点")
    private String checkOutArea;


    /**
     * 考勤结果
     */
    @ApiModelProperty(value = "考勤结果 0. 休息 1. 正常上班打卡；2. 迟到；3. 早退 4. 迟到、早退 5. 旷工；")
    private String result;

}
