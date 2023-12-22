package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 项目巡更记录(ProjectPatrolInfo)表实体类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:48:43
 */
@Data
@TableName("project_patrol_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目巡更记录")
public class ProjectPatrolInfo extends Model<ProjectPatrolInfo> {
    /*
    巡更记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "巡更记录id")
    private String patrolId;
    /*
    巡更路线id
     */
    @ApiModelProperty(value = "巡更路线id")
    private String patrolRouteId;
    /*
    巡更路线名称
     */
    @ApiModelProperty(value = "巡更路线名称")
    private String patrolRouteName;
    /**
     * 排班计划ID
     */
    @ApiModelProperty(value = "排班计划ID")
    private String schedulePlanId;
    /**
     * 排班计划名称
     */
    @ApiModelProperty(value = "排班计划名称")
    private String planName;
    /*
    巡更状态 参考字典类型 patrol_status
     */
    @ApiModelProperty(value = "巡更状态 0.待巡更 1.巡更中 2.已完成 3.已过期 参考字典类型 patrol_status")
    private String status;
    /*
    巡更日期
     */
    @ApiModelProperty(value = "巡更日期")
    private LocalDate patrolDate;


    /**
    巡更时间段
     */
    @ApiModelProperty(value = "巡更时间段")
    private String patrolTime;

    @ApiModelProperty(value = "巡更开始时间")
    private LocalTime patrolStartTime;

    @ApiModelProperty(value = "巡更结束时间")
    private LocalTime patrolEndTime;
    /**
    巡更结果 1 正常 2 异常
     */
    @ApiModelProperty(value = "巡更结果 1 正常 2 异常")
    private String result;
    /**
    已巡数量
     */
    @ApiModelProperty(value = "已巡数量")
    private Integer alreadyPatrolled;
    /**
    签到情况 1 正常 2 异常
     */
    @ApiModelProperty(value = "签到情况 1 正常 0 超时")
    private String checkInStatus;
    /**
     * 签到方式，字典check_in_type
     */
    @ApiModelProperty(value = "签到方式，1.二维码 2.现场拍照 字典check_in_type")
    private String checkInType;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
}