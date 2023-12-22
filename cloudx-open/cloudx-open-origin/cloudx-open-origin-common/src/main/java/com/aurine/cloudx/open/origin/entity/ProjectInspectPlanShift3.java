package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:26
 */
@Data
@TableName("project_inspect_plan_shift3")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)")
public class ProjectInspectPlanShift3 extends OpenBasePo<ProjectInspectPlanShift3> {

    private static final long serialVersionUID = -71121394633062534L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id")
    private String recordId;

    /**
     * 计划id，uuid
     */
    @ApiModelProperty(value = "计划id，uuid")
    private String planId;

    /**
     * 开始时间 00:00
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间 00:00
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 频次 cron
     */
    @ApiModelProperty(value = "频次 cron")
    private String frequency;

    /**
     * 当前班次的日期
     */
    @ApiModelProperty(value = "当前班次的日期")
    private String curDay;

}