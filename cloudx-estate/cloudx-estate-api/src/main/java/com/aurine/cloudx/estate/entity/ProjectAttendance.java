package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 员工考勤打卡(ProjectAttendance)表实体类
 *
 * @author xull
 * @since 2021-03-03 10:52:24
 */
@Data
@TableName("project_attendance")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工考勤打卡表")
public class ProjectAttendance extends Model<ProjectAttendance> {

    private static final long serialVersionUID = 107713526202970557L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
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
    @ApiModelProperty(value = "考勤结果 -1 没有排班计划 0.休息 1. 正常上班打卡；2. 迟到；3. 早退 4. 迟到、早退 5. 旷工；6. 下班漏打卡 7. 上班漏打卡 8. 漏打卡 迟到 9. 漏打卡 早退")
    private String result;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;



}
