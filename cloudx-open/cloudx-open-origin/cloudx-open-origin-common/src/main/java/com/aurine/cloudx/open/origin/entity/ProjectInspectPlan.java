package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:04
 */
@Data
@TableName("project_inspect_plan")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检计划设置(ProjectInspectPlan)")
public class ProjectInspectPlan extends OpenBasePo<ProjectInspectPlan> {

    private static final long serialVersionUID = 446842003293265173L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 计划id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "计划id，uuid")
    private String planId;

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;

    /**
     * 线路id
     */
    @ApiModelProperty(value = "线路id")
    private String inspectRouteId;

    /**
     * 签到方式 参考字典 inspect_check_in_type，逗号分隔
     */
    @ApiModelProperty(value = "签到方式 1.二维码 2.现场拍照 参考字典 inspect_check_in_type，逗号分隔")
    private String checkInType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDate endTime;

    /**
     * 执行周期类型 1 按天 2 按周 3 自定义
     */
    @ApiModelProperty(value = "执行周期类型 1 按天 2 按周 3 自定义")
    private String cronType;

    /**
     * 周期间隔类型 1 连续 2 间隔1
     */
    @ApiModelProperty(value = "周期间隔类型 1 连续 2 间隔1")
    private String gapType;

    /**
     * 遇周六日 1 跳过 2 正常执行
     */
    @ApiModelProperty(value = "遇周六日 1 跳过 2 正常执行")
    private String ifSatOrSun;

    /**
     * 当前状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value = "当前状态 0 已停用 1 已启用")
    private char status;

}