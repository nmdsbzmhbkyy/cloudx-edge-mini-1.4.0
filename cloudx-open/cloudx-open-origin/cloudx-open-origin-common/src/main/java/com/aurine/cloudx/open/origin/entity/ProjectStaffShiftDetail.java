package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 13:36:06
 */
@Data
@TableName("project_staff_shift_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工排班明细记录(ProjectStaffShiftDetail)")
public class ProjectStaffShiftDetail extends OpenBasePo<ProjectStaffShiftDetail> {

    private static final long serialVersionUID = 332880901434737624L;


    /**
     * 明细id,uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "明细id,uuid")
    private String detailId;

    /**
     * 排班计划id
     */
    @ApiModelProperty(value = "排班计划id")
    private String planId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String staffId;

    /**
     * 排班年份
     */
    @ApiModelProperty(value = "排班年份")
    private String planYear;

    /**
     * 排班月份
     */
    @ApiModelProperty(value = "排班月份")
    private String planMonth;

    @ApiModelProperty(value = "")
    private String day1;

    @ApiModelProperty(value = "")
    private String day2;

    @ApiModelProperty(value = "")
    private String day3;

    @ApiModelProperty(value = "")
    private String day4;

    @ApiModelProperty(value = "")
    private String day5;

    @ApiModelProperty(value = "")
    private String day6;

    @ApiModelProperty(value = "")
    private String day7;

    @ApiModelProperty(value = "")
    private String day8;

    @ApiModelProperty(value = "")
    private String day9;

    @ApiModelProperty(value = "")
    private String day10;

    @ApiModelProperty(value = "")
    private String day11;

    @ApiModelProperty(value = "")
    private String day12;

    @ApiModelProperty(value = "")
    private String day13;

    @ApiModelProperty(value = "")
    private String day14;


    @ApiModelProperty(value = "")
    private String day15;


    @ApiModelProperty(value = "")
    private String day16;

    @ApiModelProperty(value = "")
    private String day17;

    @ApiModelProperty(value = "")
    private String day18;

    @ApiModelProperty(value = "")
    private String day19;

    @ApiModelProperty(value = "")
    private String day20;

    @ApiModelProperty(value = "")
    private String day21;

    @ApiModelProperty(value = "")
    private String day22;

    @ApiModelProperty(value = "")
    private String day23;

    @ApiModelProperty(value = "")
    private String day24;

    @ApiModelProperty(value = "")
    private String day25;

    @ApiModelProperty(value = "")
    private String day26;

    @ApiModelProperty(value = "")
    private String day27;

    @ApiModelProperty(value = "")
    private String day28;

    @ApiModelProperty(value = "")
    private String day29;

    @ApiModelProperty(value = "")
    private String day30;

    @ApiModelProperty(value = "")
    private String day31;


}