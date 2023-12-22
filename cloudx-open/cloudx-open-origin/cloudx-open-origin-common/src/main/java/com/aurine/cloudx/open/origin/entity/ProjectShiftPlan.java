package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 排班计划(ProjectShiftPlan)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 10:48:33
 */
@Data
@TableName("project_shift_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "排班计划(ProjectShiftPlan)")
public class ProjectShiftPlan extends OpenBasePo<ProjectShiftPlan> {

    private static final long serialVersionUID = -48931171464435378L;

    /**
     * 排班计划id,uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "排班计划id,uuid")
    private String planId;


    /**
     * 排班计划名称
     */
    @ApiModelProperty(value = "排班计划名称")
    private String planName;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;
    /**
     * 周一班次
     */
    @ApiModelProperty(value = "周一班次")
    private String mon;
    /**
     * 周二班次
     */
    @ApiModelProperty(value = "周二班次")
    private String tue;
    /**
     * 周三班次
     */
    @ApiModelProperty(value = "周三班次")
    private String wed;
    /**
     * 周四班次
     */
    @ApiModelProperty(value = "周四班次")
    private String thur;
    /**
     * 周五班次
     */
    @ApiModelProperty(value = "周五班次")
    private String fri;
    /**
     * 周六班次
     */
    @ApiModelProperty(value = "周六班次")
    private String sat;
    /**
     * 周天班次
     */
    @ApiModelProperty(value = "周天班次")
    private String sun;
    /**
     * 节假日自动排休
     */
    @ApiModelProperty(value = "节假日自动排休 1是 0否")
    private String autoSchedule;
    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private String shiftId;

    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次描述")
    private String shiftDesc;


}